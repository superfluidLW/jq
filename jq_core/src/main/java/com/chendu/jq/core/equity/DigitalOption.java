package com.chendu.jq.core.equity;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.dayCount.DayCount;
import com.chendu.jq.core.common.jqEnum.*;
import com.chendu.jq.core.equity.calculator.analytical.EuropeanDigitalCalculator;
import com.chendu.jq.core.equity.calculator.mc.MonteCarloCalculator;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.mktObj.JqTicker;
import com.chendu.jq.core.util.JsonUtils;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class DigitalOption extends Option {
    private Double digitalPayoff;

    public DigitalOption(){
        super();
        tradeType = TradeType.DigitalOption;
    }

    @Override
    public Option bumpMaturity(int offset) {
        DigitalOption vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), DigitalOption.class);
        vo.setMaturityDate(this.maturityDate.plusDays(offset));
        return vo;
    }

    @Override
    public Double[][] payOffChart() {
        return new Double[0][];
    }

    @Override
    public Double calcPayOff(LinkedHashMap<LocalDate, Double> path) {
        LocalDate exerciseDate = exerciseDates.get(0);
        Double price = path.get(exerciseDate);
        return optionDirection == OptionDirection.Call ? (price>strike ? notional : 0.0) : (price < strike ? notional : 0.0);
    }

    @Override
    public JqResult calc(JqMarket jqMarket) {
        JqResult jqResult = new JqResult();

        if(valuationModel == ValuationModel.MonteCarlo){
            MonteCarloCalculator mc = new MonteCarloCalculator();
            return mc.calc(this, jqMarket);
        }
        else if(valuationModel == ValuationModel.Analytical){
            EuropeanDigitalCalculator edc = new EuropeanDigitalCalculator();
            return edc.calc(this, jqMarket);
        }

        return jqResult;
    }

    @Override
    public List<JqCashflow> cashflows(JqMarket jqMarket) {
        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
        return null;
    }

    public static String[][] templateTradeData() {
        DigitalOption digitalOption = new DigitalOption();
        digitalOption.setStartDate(LocalDate.now());
        digitalOption.setMaturityDate(LocalDate.now().plusYears(1));
        digitalOption.setExerciseDates(Arrays.asList(LocalDate.now().plusYears(1)));
        digitalOption.setObserveDates(Arrays.asList(LocalDate.now().plusYears(1)));
        digitalOption.setStrike(1000.0);
        digitalOption.setOptionDirection(OptionDirection.Call);
        digitalOption.setUnderlyingTicker(new JqTicker("SH000300"));
        digitalOption.setDayCount(new DayCount(DayCountType.Act365));
        digitalOption.setNotional(1.0);
        digitalOption.setDomCurrency(Currency.Cny);
        digitalOption.setValuationModel(ValuationModel.Analytical);
        return JqTrade.templateTradeData(DigitalOption.class, digitalOption);
    }

    @Override
    public List<JqCashflow> calcPayoff() {
        return null;
    }
}
