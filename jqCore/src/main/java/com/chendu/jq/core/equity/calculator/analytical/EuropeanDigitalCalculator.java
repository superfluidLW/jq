package com.chendu.jq.core.equity.calculator.analytical;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.jqEnum.OptionDirection;
import com.chendu.jq.core.equity.DigitalOption;
import com.chendu.jq.core.equity.calculator.OptionCalculator;
import com.chendu.jq.core.market.JqMarket;

import java.time.LocalDate;

public class EuropeanDigitalCalculator extends OptionCalculator {
    @Override
    public Double calcPv(JqTrade trade, JqMarket jqMarket) {
        DigitalOption digitalOption = (DigitalOption) trade;

        LocalDate exerciseDate = digitalOption.getExerciseDate();
        LocalDate maturityDate = digitalOption.getMaturityDate();

        Double exerciseTime = digitalOption.getDayCount().yearFraction(jqMarket.getMktDate(), exerciseDate);
        Double maturityTime = digitalOption.getDayCount().yearFraction(jqMarket.getMktDate(), maturityDate);

        Double dfAtExercise = jqMarket.jqCurve(digitalOption.getDomCurrency()).getDf(exerciseTime);
        Double dividendDfAtExercise = jqMarket.getDividendCurveMap().get(digitalOption.getUnderlyingTicker()).getDf(exerciseTime);

        Double dfAtMaturity = jqMarket.jqCurve(digitalOption.getDomCurrency()).getDf(maturityTime);
        Double dfExercise2Maturity = dfAtMaturity / dfAtExercise;

        Double vol = jqMarket.tickerVol(digitalOption.getUnderlyingTicker());
        Double s0 = jqMarket.tickerPrice(digitalOption.getUnderlyingTicker());
        Double f = s0 * dividendDfAtExercise / dfAtExercise;

        Double notional = digitalOption.getNotional();
        Double strike = digitalOption.getStrike();

        Double d1 = (Math.log(f / strike) + vol * vol / 2.0 * exerciseTime) / (vol * Math.sqrt(exerciseTime));
        Double d2 = d1 - vol * Math.sqrt(exerciseTime);
        Double nd1 = normal.cumulativeProbability(d1);
        Double nd2 = normal.cumulativeProbability(d2);

        OptionDirection optionDirection = digitalOption.getOptionDirection();

        return optionDirection == OptionDirection.Call
                ? notional * dfExercise2Maturity * dfAtExercise * (nd2 * digitalOption.getDigitalPayoff())
                : notional * dfExercise2Maturity * dfAtExercise * (1 - nd2) * digitalOption.getDigitalPayoff();
    }
}
