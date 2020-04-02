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

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class VanillaOption extends Option {
    public VanillaOption(){
        super();
        tradeType = TradeType.VanillaOption;
    }

    @Override
    public Option bumpMaturity(int offset) {
        VanillaOption vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), VanillaOption.class);
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
        return (optionDirection == OptionDirection.Call ? Math.max(price-strike, 0.0) : Math.max(strike-price, 0.0))*notional;
    }

    @Override
    public JqResult calc(JqMarket jqMarket) {
        if(valuationModel == ValuationModel.MonteCarlo){
            MonteCarloCalculator mc = new MonteCarloCalculator();
            return mc.calc(this, jqMarket);
        }
        else{
            EuropeanVanillaCalculator evc = new EuropeanVanillaCalculator();
            return evc.calc(this, jqMarket);
        }
    }

    @Override
    public List<JqCashflow> cashflows(JqMarket jqMarket) {
        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
        return null;
    }

    public static String[][] templateTradeData() {
        VanillaOption vanillaOption = new VanillaOption();
        vanillaOption.setStartDate(LocalDate.now());
        vanillaOption.setMaturityDate(LocalDate.now().plusYears(1));
        vanillaOption.setExerciseDate(LocalDate.now().plusYears(1));
        vanillaOption.setStrike(1000.0);
        vanillaOption.setOptionDirection(OptionDirection.Call);
        vanillaOption.setUnderlyingTicker(new JqTicker("SH000300"));
        vanillaOption.setDayCount(new DayCount(DayCountType.Act365));
        vanillaOption.setNotional(1.0);
        vanillaOption.setDomCurrency(Currency.Cny);
        vanillaOption.setValuationModel(ValuationModel.Analytical);
        return JqTrade.templateTradeData(VanillaOption.class, vanillaOption);
    }

    @Override
    public List<JqCashflow> calcPayoff() {
        return null;
    }
}
