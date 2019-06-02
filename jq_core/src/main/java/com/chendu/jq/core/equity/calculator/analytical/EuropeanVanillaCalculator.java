package com.chendu.jq.core.equity.calculator.analytical;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.jqEnum.OptionDirection;
import com.chendu.jq.core.equity.VanillaOption;
import com.chendu.jq.core.equity.calculator.OptionCalculator;
import com.chendu.jq.core.market.JqMarket;

import java.time.LocalDate;

public class EuropeanVanillaCalculator extends OptionCalculator {
    @Override
    public Double calcPv(JqTrade trade, JqMarket jqMarket){
        VanillaOption vanillaOption = (VanillaOption) trade;

        LocalDate exerciseDate = vanillaOption.getExerciseDates().get(0);
        LocalDate maturityDate = vanillaOption.getMaturityDate();

        Double exerciseTime = vanillaOption.getDayCount().yearFraction(jqMarket.getMktDate(), exerciseDate);
        Double maturityTime = vanillaOption.getDayCount().yearFraction(jqMarket.getMktDate(), maturityDate);

        Double dfAtExercise = jqMarket.jqCurve(vanillaOption.getDomCurrency()).getDf(exerciseTime);
        Double dividendDfAtExercise = jqMarket.getDividendCurveMap().get(vanillaOption.getUnderlyingTicker()).getDf(exerciseTime);

        Double dfAtMaturity = jqMarket.jqCurve(vanillaOption.getDomCurrency()).getDf(maturityTime);
        Double dfExercise2Maturity = dfAtMaturity / dfAtExercise;

        Double vol = jqMarket.tickerVol(vanillaOption.getUnderlyingTicker());
        Double s0 = jqMarket.tickerPrice(vanillaOption.getUnderlyingTicker());
        Double f = s0 * dividendDfAtExercise / dfAtExercise;

        Double notional = vanillaOption.getNotional();
        Double strike = vanillaOption.getStrike();

        Double d1 = (Math.log(f / strike) + vol * vol / 2.0 * exerciseTime) / (vol * Math.sqrt(exerciseTime));
        Double d2 = d1 - vol * Math.sqrt(exerciseTime);
        Double nd1 = normal.cumulativeProbability(d1);
        Double nd2 = normal.cumulativeProbability(d2);

        OptionDirection optionDirection = vanillaOption.getOptionDirection();

        return optionDirection == OptionDirection.Call
                ? notional * dfExercise2Maturity * dfAtExercise * (f * nd1 - strike * nd2)
                : notional * dfExercise2Maturity * dfAtExercise * (strike * (1 - nd2) - f * (1 - nd1));
    }
}
