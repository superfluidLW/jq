package com.jq.impl.calculator.equity;

import com.jq.common.deal.Deal;
import com.jq.common.convention.OptionDirection;
import com.jq.common.deal.equity.VanillaOption;
import com.jq.common.market.Market;
import com.jq.impl.convention.DayCountImpl;


import java.time.LocalDate;

public class VanillaOptionCalculator extends OptionCalculator {
    @Override
    public Double calcPv(Deal trade, Market market){
        VanillaOption option = (VanillaOption) trade;

        LocalDate exerciseDate = option.getExerciseDate();
        LocalDate maturityDate = option.getMaturityDate();

        Double exerciseTime = DayCountImpl.get(option.getDayCount()).yearFraction(market.getMktDate(), exerciseDate);
        Double maturityTime = DayCountImpl.get(option.getDayCount()).yearFraction(market.getMktDate(), maturityDate);

        Double dfAtExercise = market.jqCurve(option.getDomCurrency()).getDf(exerciseTime);
        Double dividendDfAtExercise = market.getDividendCurveMap().get(option.getUnderlying()).getDf(exerciseTime);

        Double dfAtMaturity = market.jqCurve(option.getDomCurrency()).getDf(maturityTime);
        Double dfExercise2Maturity = dfAtMaturity / dfAtExercise;

        Double vol = market.tickerVol(option.getUnderlying());
        Double s0 = market.tickerPrice(option.getUnderlying());
        Double f = s0 * dividendDfAtExercise / dfAtExercise;

        Double notional = option.getNotional();
        Double strike = option.getStrike();

        Double d1 = (Math.log(f / strike) + vol * vol / 2.0 * exerciseTime) / (vol * Math.sqrt(exerciseTime));
        Double d2 = d1 - vol * Math.sqrt(exerciseTime);
        Double nd1 = normal.cumulativeProbability(d1);
        Double nd2 = normal.cumulativeProbability(d2);

        OptionDirection optionDirection = option.getOptionDirection();

        return optionDirection == OptionDirection.Call
                ? notional * dfExercise2Maturity * dfAtExercise * (f * nd1 - strike * nd2)
                : notional * dfExercise2Maturity * dfAtExercise * (strike * (1 - nd2) - f * (1 - nd1));
    }
}
