package com.chendu.jq.core.equity;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.dayCount.DayCount;
import com.chendu.jq.core.common.jqEnum.*;
import com.chendu.jq.core.equity.calculator.analytical.DoubleBarrierCalculator;
import com.chendu.jq.core.equity.calculator.mc.MonteCarloDKoCalculator;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.mktObj.JqTicker;
import com.chendu.jq.core.util.JsonUtils;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class DoubleBarrierOption extends Option {
    public Double koRebate;
    public Double kiCoupon;
    public Double lBarrier;
    public Double uBarrier;
    public Double participationRate;

    public DoubleBarrierOption(){
        super();
        tradeType = TradeType.DoubleBarrierOption;
    }

    @Override
    public Option bumpMaturity(int offset) {
        DoubleBarrierOption vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), DoubleBarrierOption.class);
        vo.setMaturityDate(this.maturityDate.plusDays(offset));
        vo.setExerciseDate(this.exerciseDate.plusDays(offset));
        return vo;
    }

    @Override
    public Double[][] payOffChart() {
        return new Double[0][];
    }

    @Override
    public Double calcPayOff(LinkedHashMap<LocalDate, Double> path) {
        return null;
    }

    @Override
    public JqResult calc(JqMarket jqMarket) {
        JqResult jqResult = new JqResult();

        if(valuationModel == ValuationModel.MonteCarlo){
            MonteCarloDKoCalculator mc = new MonteCarloDKoCalculator(300, 10000);
            return mc.calc(this, jqMarket);
        }
        else if(valuationModel == ValuationModel.Analytical){
            DoubleBarrierCalculator dbc = new DoubleBarrierCalculator();
            return dbc.calc(this, jqMarket);
        }

        return jqResult;
    }

    @Override
    public List<JqCashflow> cashflows(JqMarket jqMarket) {
        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
        return null;
    }

    public static String[][] templateTradeData() {
        DoubleBarrierOption doubleBarrierOption = new DoubleBarrierOption();
        doubleBarrierOption.setStartDate(LocalDate.now());
        doubleBarrierOption.setMaturityDate(LocalDate.now().plusYears(1));
        doubleBarrierOption.setExerciseDate(LocalDate.now().plusYears(1));
        doubleBarrierOption.setOptionDirection(OptionDirection.Call);
        doubleBarrierOption.setUnderlyingTicker(new JqTicker("SH000300"));
        doubleBarrierOption.setDayCount(new DayCount(DayCountType.Act365));
        doubleBarrierOption.setNotional(1.0);
        doubleBarrierOption.setDomCurrency(Currency.Cny);
        doubleBarrierOption.setLBarrier(900.0);
        doubleBarrierOption.setUBarrier(1100.0);
        doubleBarrierOption.setKoRebate(0.025);
        doubleBarrierOption.setKiCoupon(0.03);
        doubleBarrierOption.setValuationModel(ValuationModel.Analytical);
        return JqTrade.templateTradeData(DoubleBarrierOption.class, doubleBarrierOption);
    }

    @Override
    public List<JqCashflow> calcPayoff() {
        return null;
    }
}
