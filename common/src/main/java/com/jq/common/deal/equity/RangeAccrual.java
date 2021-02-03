package com.jq.common.deal.equity;

import com.jq.common.convention.Currency;
import com.jq.common.convention.OptionDirection;
import com.jq.common.convention.ValuationModel;
import com.jq.common.deal.DealType;
import com.jq.common.market.Market;
import com.jq.common.output.CashFlow;
import com.jq.common.output.Result;
import com.jq.common.deal.Deal;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class RangeAccrual extends Option {
    public Double lRange;
    public Double uRange;
    public Double coupon;
//    public static Calendar calendar = new Calendar(Venue.Cib);
    public RangeAccrual(){
        super();
        dealType = DealType.RangeAccrual;
    }

    public List<LocalDate> obsDates() {
        List<LocalDate> obsDates = new ArrayList<>();
//        for (LocalDate date = startDate; date.isBefore(exerciseDate) || date.equals(exerciseDate); date = date.plusDays(1)) {
//            if (calendar.isBizDay(date)) {
//                obsDates.add(date);
//            }
//        }

        return obsDates;
    }

    @Override
    public Option bumpMaturity(int offset) {
//        RangeAccrual vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), RangeAccrual.class);
//        vo.setMaturityDate(this.maturityDate.plusDays(offset));
//        vo.setExerciseDate(exerciseDate.plusDays(offset));
//        return vo;
        return null;
    }

    @Override
    public Double[][] payOffChart() {
        double delta = 4*(uRange-lRange)/40.0;
        Double[][] payoff = new Double[40][2];
        int offset = payoff.length/2;
        for(int i = 0; i < payoff.length; ++i){
            Double price = strike + (i-offset) * delta;
            Double po = (price > lRange && price < uRange) ? coupon : 0.0;
            payoff[i][0] = price;
            payoff[i][1] = po;
        }
        return payoff;
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

    public Result calc(Market market) {
        Result result = new Result();

//        if(valuationModel == ValuationModel.MonteCarlo){
//            MonteCarloCalculator mc = new MonteCarloCalculator();
//            return mc.calc(this, jqMarket);
//        }
//        else if(valuationModel == ValuationModel.Analytical){
//            RangeAccrualCalculator rac = new RangeAccrualCalculator();
//            return rac.calc(this, jqMarket);
//        }

        return result;
    }

    public List<CashFlow> cashflows(Market market) {
//        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
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
//        rangeAccrual.setUnderlyingTicker(new JqTicker("SH000300"));
//        rangeAccrual.setDayCountImpl(new Act365());
        rangeAccrual.setNotional(1.0);
        rangeAccrual.setDomCurrency(Currency.Cny);
        rangeAccrual.setLRange(950.0);
        rangeAccrual.setURange(1025.0);
        rangeAccrual.setStrike(995.0);
        rangeAccrual.setCoupon(0.025);
        rangeAccrual.setValuationModel(ValuationModel.Analytical);


        return Deal.templateTradeData(RangeAccrual.class, rangeAccrual);
    }

    @Override
    public List<CashFlow> calcPayoff() {
        return null;
    }
}
