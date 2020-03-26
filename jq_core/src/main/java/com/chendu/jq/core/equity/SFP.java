package com.chendu.jq.core.equity;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.dayCount.DayCount;
import com.chendu.jq.core.common.jqEnum.*;
import com.chendu.jq.core.equity.calculator.analytical.EuropeanVanillaCalculator;
import com.chendu.jq.core.equity.calculator.mc.MonteCarloCalculator;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.mktObj.JqTicker;
import com.chendu.jq.core.util.JsonUtils;
import lombok.Data;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class SFP extends Option {
    public static ScriptEngineManager mgr = new ScriptEngineManager();
    public static ScriptEngine engine=mgr.getEngineByName("JavaScript");

    public SFP(){
        super();
        tradeType = TradeType.SFP;
    }

    @Override
    public Option bumpMaturity(int offset) {
        SFP vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), SFP.class);
        vo.setMaturityDate(this.maturityDate.plusDays(offset));
        vo.setExerciseDate(exerciseDate.plusDays(offset));
        return vo;
    }

    @Override
    public Double[][] payOffChart() {
        double delta = strike * 0.01;
        Double[][] payoff = new Double[40][2];
        int offset = payoff.length/2;
        for(int i = 0; i < payoff.length; ++i){
            Double price = strike + (i-offset) * delta;
            Double po = optionDirection == OptionDirection.Call ? Math.max(price-strike, 0) : Math.max(strike-price, 0);
            payoff[i][0] = price;
            payoff[i][1] = po;
        }
        return payoff;
    }

    @Override
    public Double calcPayOff(LinkedHashMap<LocalDate, Double> path) {

        Double price = path.get(exerciseDate);
        return optionDirection == OptionDirection.Call ? Math.max(price-strike, 0.0) : Math.max(strike-price, 0.0);
    }

    @Override
    public JqResult calc(JqMarket jqMarket) {
        throw new NotImplementedException();
    }

    @Override
    public List<JqCashflow> cashflows(JqMarket jqMarket) {
        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
        return null;
    }

    public static String[][] templateTradeData() {
        SFP sfp = new SFP();
        sfp.setStartDate(LocalDate.now());
        sfp.setMaturityDate(LocalDate.now().plusYears(1));
        sfp.setExerciseDate(LocalDate.now().plusYears(1));
        sfp.setStrike(1000.0);
        sfp.setOptionDirection(OptionDirection.Call);
        sfp.setUnderlyingTicker(new JqTicker("SH000300"));
        sfp.setDayCount(new DayCount(DayCountType.Act365));
        sfp.setNotional(1.0);
        sfp.setDomCurrency(Currency.Cny);
        sfp.setValuationModel(ValuationModel.Analytical);
        return JqTrade.templateTradeData(SFP.class, sfp);
    }

    @Override
    public List<JqCashflow> calcPayoff() {
        return null;
    }
}
