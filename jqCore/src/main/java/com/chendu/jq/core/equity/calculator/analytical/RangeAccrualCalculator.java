package com.chendu.jq.core.equity.calculator.analytical;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCalendar;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.dayCount.DayCount;
import com.chendu.jq.core.common.jqEnum.*;
import com.chendu.jq.core.equity.DigitalOption;
import com.chendu.jq.core.equity.RangeAccrual;
import com.chendu.jq.core.equity.VanillaOption;
import com.chendu.jq.core.equity.calculator.OptionCalculator;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.mktObj.JqTicker;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class RangeAccrualCalculator extends OptionCalculator {
    public static EuropeanDigitalCalculator europeanDigitalCalculator = new EuropeanDigitalCalculator();

    @Override
    public Double calcPv(JqTrade trade, JqMarket jqMarket){
        RangeAccrual rangeAccrual = (RangeAccrual) trade;

        Double pv = 0.0;
        List<LocalDate> obsDates = rangeAccrual.obsDates();
        for (LocalDate date : obsDates) {
            DigitalOption digitalOption1 = new DigitalOption();
            digitalOption1.setStartDate(rangeAccrual.startDate);
            digitalOption1.setMaturityDate(rangeAccrual.maturityDate);
            digitalOption1.setExerciseDate(date);
            digitalOption1.setStrike(rangeAccrual.lRange);
            digitalOption1.setOptionDirection(OptionDirection.Call);
            digitalOption1.setUnderlyingTicker(rangeAccrual.underlyingTicker);
            digitalOption1.setDayCount(rangeAccrual.dayCount);
            digitalOption1.setDigitalPayoff(1.0);
            digitalOption1.setNotional(1.0);
            digitalOption1.setDomCurrency(rangeAccrual.domCurrency);
            digitalOption1.setValuationModel(ValuationModel.Analytical);

            DigitalOption digitalOption2 = new DigitalOption();
            digitalOption2.setStartDate(rangeAccrual.startDate);
            digitalOption2.setMaturityDate(rangeAccrual.maturityDate);
            digitalOption2.setExerciseDate(date);
            digitalOption2.setStrike(rangeAccrual.uRange);
            digitalOption2.setOptionDirection(OptionDirection.Call);
            digitalOption2.setUnderlyingTicker(rangeAccrual.underlyingTicker);
            digitalOption2.setDayCount(rangeAccrual.dayCount);
            digitalOption2.setDigitalPayoff(1.0);
            digitalOption2.setNotional(1.0);
            digitalOption2.setDomCurrency(rangeAccrual.domCurrency);
            digitalOption2.setValuationModel(ValuationModel.Analytical);

            pv += (europeanDigitalCalculator.calcPv(digitalOption1, jqMarket) - europeanDigitalCalculator.calcPv(digitalOption2, jqMarket)) * rangeAccrual.coupon * rangeAccrual.notional;
        }

        return pv/obsDates.size();
    }
}
