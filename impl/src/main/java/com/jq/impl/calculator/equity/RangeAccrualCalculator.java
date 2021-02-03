package com.jq.impl.calculator.equity;

import com.jq.common.deal.Deal;
import com.jq.common.convention.OptionDirection;
import com.jq.common.convention.ValuationModel;
import com.jq.common.deal.equity.DigitalOption;
import com.jq.common.deal.equity.RangeAccrual;
import com.jq.common.market.Market;

import java.time.LocalDate;
import java.util.List;

public class RangeAccrualCalculator extends OptionCalculator {
    public static DigitalOptionCalculator digitalOptionCalculator = new DigitalOptionCalculator();

    @Override
    public Double calcPv(Deal trade, Market market){
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
            digitalOption1.setUnderlying(rangeAccrual.getUnderlying());
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
            digitalOption1.setUnderlying(rangeAccrual.getUnderlying());
            digitalOption1.setDayCount(rangeAccrual.dayCount);
            digitalOption2.setDigitalPayoff(1.0);
            digitalOption2.setNotional(1.0);
            digitalOption2.setDomCurrency(rangeAccrual.domCurrency);
            digitalOption2.setValuationModel(ValuationModel.Analytical);

            pv += (digitalOptionCalculator.calcPv(digitalOption1, market) - digitalOptionCalculator.calcPv(digitalOption2, market)) * rangeAccrual.coupon * rangeAccrual.notional;
        }

        return pv/obsDates.size();
    }
}
