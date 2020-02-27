package com.chendu.jq.core.equity;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCalendar;
import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.dayCount.DayCount;
import com.chendu.jq.core.common.jqEnum.*;
import com.chendu.jq.core.equity.calculator.analytical.RangeAccrualCalculator;
import com.chendu.jq.core.equity.calculator.mc.MonteCarloCalculator;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.mktObj.JqTicker;
import com.chendu.jq.core.util.JsonUtils;
import lombok.Data;

import java.awt.image.AreaAveragingScaleFilter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class RangeAccrual extends Option {
    public Double lRange;
    public Double uRange;
    public Double coupon;
    public static JqCalendar jqCalendar = new JqCalendar(Venue.Cib);
    public RangeAccrual(){
        super();
        tradeType = TradeType.RangeAccrual;
    }

    public List<LocalDate> obsDates() {
        List<LocalDate> obsDates = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(exerciseDate) || date.equals(exerciseDate); date = date.plusDays(1)) {
            if (jqCalendar.isBizDay(date)) {
                obsDates.add(date);
            }
        }

        return obsDates;
    }

    @Override
    public Option bumpMaturity(int offset) {
        RangeAccrual vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), RangeAccrual.class);
        vo.setMaturityDate(this.maturityDate.plusDays(offset));
        vo.setExerciseDate(exerciseDate.plusDays(offset));
        return vo;
    }

    @Override
    public Double[][] payOffChart() {
        return new Double[0][];
    }

    @Override
    public Double calcPayOff(LinkedHashMap<LocalDate, Double> path) {
        Integer count = 1;
        List<LocalDate> obsDates = obsDates();
        for (LocalDate date:obsDates
             ) {
            Double value = path.get(date);
            if (value >= lRange && value < uRange) {
                count ++;
            }
        }
        return notional * coupon * count / obsDates.size();
    }

    @Override
    public JqResult calc(JqMarket jqMarket) {
        JqResult jqResult = new JqResult();

        if(valuationModel == ValuationModel.MonteCarlo){
            MonteCarloCalculator mc = new MonteCarloCalculator();
            return mc.calc(this, jqMarket);
        }
        else if(valuationModel == ValuationModel.Analytical){
            RangeAccrualCalculator rac = new RangeAccrualCalculator();
            return rac.calc(this, jqMarket);
        }

        return jqResult;
    }

    @Override
    public List<JqCashflow> cashflows(JqMarket jqMarket) {
        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
        return null;
    }

    public static String[][] templateTradeData() {
        RangeAccrual rangeAccrual = new RangeAccrual();
        rangeAccrual.setStartDate(LocalDate.now());
        rangeAccrual.setMaturityDate(LocalDate.now().plusYears(1));
        rangeAccrual.setExerciseDate(LocalDate.now().plusYears(1));

//        List<LocalDate> obsDates = new ArrayList<>();
//        for(int i = 1; i <= 11; ++i){
//            obsDates.add(LocalDate.now().plusMonths(i));
//        }
//        obsDates.add(LocalDate.now().plusYears(1));
//        rangeAccrual.setObserveDates(obsDates);
        rangeAccrual.setOptionDirection(OptionDirection.Call);
        rangeAccrual.setUnderlyingTicker(new JqTicker("SH000300"));
        rangeAccrual.setDayCount(new DayCount(DayCountType.Act365));
        rangeAccrual.setNotional(1.0);
        rangeAccrual.setDomCurrency(Currency.Cny);
        rangeAccrual.setLRange(3000.0);
        rangeAccrual.setLRange(3050.0);
        rangeAccrual.setLRange(0.025);
        rangeAccrual.setValuationModel(ValuationModel.Analytical);
        return JqTrade.templateTradeData(RangeAccrual.class, rangeAccrual);
    }

    @Override
    public List<JqCashflow> calcPayoff() {
        return null;
    }
}
