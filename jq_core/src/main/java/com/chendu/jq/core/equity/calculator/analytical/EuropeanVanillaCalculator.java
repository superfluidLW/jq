package com.chendu.jq.core.equity.calculator.analytical;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqEnum.OptionDirection;
import com.chendu.jq.core.common.jqInterface.ICalculator;
import com.chendu.jq.core.equity.VanillaOption;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.util.JqLog;
import org.apache.commons.math3.distribution.NormalDistribution;
import sun.security.pkcs11.wrapper.CK_NOTIFY;

import java.time.LocalDate;

public class EuropeanVanillaCalculator implements ICalculator {

    public static NormalDistribution normal = new NormalDistribution(0.0, 1.0);

    @Override
    public JqResult calculate(JqTrade trade, JqMarket jqMarket) {
        if(! (trade instanceof VanillaOption)){
            JqLog.info("Used to valuate European vanilla Option, cannot valuate", this.getClass().getName(), trade.getClass().getName());
        }

        VanillaOption vanillaOption = (VanillaOption)trade;
        JqResult jqResult = new JqResult();

        LocalDate exerciseDate = vanillaOption.getExerciseDates().get(0);
        LocalDate maturityDate = vanillaOption.getMaturityDate();

        Double exerciseTime = vanillaOption.getDayCount().yearFraction(jqMarket.getMktDate(), exerciseDate);
        Double maturityTime = vanillaOption.getDayCount().yearFraction(jqMarket.getMktDate(), maturityDate);

        Double dfAtExercise = jqMarket.getYieldCurveMap().get(vanillaOption.getDomCurrency()).getDf(exerciseTime);
        Double dividendDfAtExercise = jqMarket.getDividendCurveMap().get(vanillaOption.getUnderlyingAssetSymbol()).getDf(exerciseTime);

        Double dfAtMaturity = jqMarket.getYieldCurveMap().get(vanillaOption.getDomCurrency()).getDf(maturityTime);
        Double dfExercise2Maturity = dfAtMaturity / dfAtExercise;

        Double vol = jqMarket.getVolatilityMap().get(vanillaOption.getUnderlyingAssetSymbol());
        Double s0 = jqMarket.getQuoteMap().get(vanillaOption.getUnderlyingAssetSymbol());
        Double f = s0*dividendDfAtExercise/dfAtExercise;

        Double notional = vanillaOption.getNotional();
        Double strike = vanillaOption.getStrike();

        Double d1 = (Math.log(f/strike) + vol*vol/2.0*exerciseTime)/(vol*Math.sqrt(exerciseTime));
        Double d2 = d1 - vol*Math.sqrt(exerciseTime);
        Double nd1 = normal.cumulativeProbability(d1);
        Double nd2 = normal.cumulativeProbability(d2);
        Double nd1Prime = 1.0 / Math.sqrt(2.0 * Math.PI) * Math.exp(-d1 * d1 / 2.0);
        Double nd2Prime = 1.0 / Math.sqrt(2.0 * Math.PI) * Math.exp(-d2 * d2 / 2.0);


        OptionDirection optionDirection = vanillaOption.getOptionDirection();
        Double pv = optionDirection == OptionDirection.Call
                ? notional * dfExercise2Maturity * dfAtExercise * (f*nd1 - strike*nd2)
                :  notional*dfExercise2Maturity*dfAtExercise*(strike*(1 - nd2) - f*(1 - nd1));

        jqResult.setPv(pv);
        return jqResult;
    }
}
