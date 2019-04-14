package com.chendu.jq.core.equity.calculator.analytical;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqEnum.OptionDirection;
import com.chendu.jq.core.common.jqInterface.ICalculator;
import com.chendu.jq.core.equity.VanillaOption;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.util.JqLog;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.time.LocalDate;

public class EuropeanVanillaCalculator implements ICalculator {

    public static NormalDistribution normal = new NormalDistribution(0.0, 1.0);

    @Override
    public JqResult calc(JqTrade trade, JqMarket jqMarket) {
        if (!(trade instanceof VanillaOption)) {
            JqLog.info("Used to valuate European vanilla Option, cannot valuate", this.getClass().getName(), trade.getClass().getName());
        }

        VanillaOption vanillaOption = (VanillaOption) trade;
        JqResult jqResult = new JqResult();

        LocalDate exerciseDate = vanillaOption.getExerciseDates().get(0);
        LocalDate maturityDate = vanillaOption.getMaturityDate();

        Double exerciseTime = vanillaOption.getDayCount().yearFraction(jqMarket.getMktDate(), exerciseDate);
        Double maturityTime = vanillaOption.getDayCount().yearFraction(jqMarket.getMktDate(), maturityDate);

        Double dfAtExercise = jqMarket.getYieldCurveMap().get(vanillaOption.getDomCurrency()).getDf(exerciseTime);
        Double dividendDfAtExercise = jqMarket.getDividendCurveMap().get(vanillaOption.getUnderlyingTicker()).getDf(exerciseTime);

        Double dfAtMaturity = jqMarket.getYieldCurveMap().get(vanillaOption.getDomCurrency()).getDf(maturityTime);
        Double dfExercise2Maturity = dfAtMaturity / dfAtExercise;

        Double vol = jqMarket.tickerPrice(vanillaOption.getUnderlyingTicker());
        Double s0 = jqMarket.tickerPrice(vanillaOption.getUnderlyingTicker());
        Double f = s0 * dividendDfAtExercise / dfAtExercise;

        Double notional = vanillaOption.getNotional();
        Double strike = vanillaOption.getStrike();

        Double d1 = (Math.log(f / strike) + vol * vol / 2.0 * exerciseTime) / (vol * Math.sqrt(exerciseTime));
        Double d2 = d1 - vol * Math.sqrt(exerciseTime);
        Double nd1 = normal.cumulativeProbability(d1);
        Double nd2 = normal.cumulativeProbability(d2);
        Double nd1Prime = 1.0 / Math.sqrt(2.0 * Math.PI) * Math.exp(-d1 * d1 / 2.0);
        Double nd2Prime = 1.0 / Math.sqrt(2.0 * Math.PI) * Math.exp(-d2 * d2 / 2.0);


        OptionDirection optionDirection = vanillaOption.getOptionDirection();
        Double pv = optionDirection == OptionDirection.Call
                ? notional * dfExercise2Maturity * dfAtExercise * (f * nd1 - strike * nd2)
                : notional * dfExercise2Maturity * dfAtExercise * (strike * (1 - nd2) - f * (1 - nd1));

        Double delta = optionDirection == OptionDirection.Call
                ? notional * dfExercise2Maturity * dividendDfAtExercise * nd1
                : notional * dfExercise2Maturity * dividendDfAtExercise * (nd1 - 1);

        Double gamma = notional*nd1Prime*dfExercise2Maturity*dividendDfAtExercise/(s0*vol*Math.sqrt(exerciseTime));

        Double vega = notional * s0 * Math.sqrt(exerciseTime) * nd1Prime * dfExercise2Maturity * dividendDfAtExercise * 0.01;

        Double rho = optionDirection == optionDirection.Call
                ? notional*strike*maturityTime*dfAtExercise*nd2/10000
                : -notional*strike*maturityTime*dfAtExercise*(1 - nd2)/10000;


        jqResult.setPv(pv);
        jqResult.setDelta(delta);
        jqResult.setVega(vega);
        jqResult.setRho(rho);
        return jqResult;
    }
}
