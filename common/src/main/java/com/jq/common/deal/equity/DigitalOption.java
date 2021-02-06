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
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class DigitalOption extends Option {

    public Double digitalPayoff;

    public DigitalOption(){
        super();
        dealType = DealType.DigitalOption;
    }

    @Override
    public Option bumpMaturity(int offset) {
//        DigitalOption vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), DigitalOption.class);
//        vo.setMaturityDate(this.maturityDate.plusDays(offset));
//        vo.setExerciseDate(exerciseDate.plusDays(offset));
//        return vo;
        return null;
    }

    @Override
    public Double[][] payOffChart() {
        double delta = strike * 0.01;
        Double[][] payoff = new Double[40][2];
        int offset = payoff.length/2;
        for(int i = 0; i < payoff.length; ++i){
            Double price = strike + (i-offset) * delta;
            Double po = optionDirection == OptionDirection.Call ? (price>strike ? notional*digitalPayoff : 0.0) : (price < strike ? notional*digitalPayoff : 0.0);
            payoff[i][0] = price;
            payoff[i][1] = po;
        }
        return payoff;
    }

    @Override
    public Double calcPayOff(LinkedHashMap<LocalDate, Double> path) {
        Double price = path.get(exerciseDate);
        return optionDirection == OptionDirection.Call ? (price>strike ? notional*digitalPayoff : 0.0) : (price < strike ? notional*digitalPayoff : 0.0);
    }

    public Result calc(Market market) {
        Result result = new Result();

//        if(valuationModel == ValuationModel.MonteCarlo){
//            MonteCarloCalculator mc = new MonteCarloCalculator();
//            return mc.calc(this, jqMarket);
//        }
//        else if(valuationModel == ValuationModel.Analytical){
//            EuropeanDigitalCalculator edc = new EuropeanDigitalCalculator();
//            return edc.calc(this, jqMarket);
//        }

        return result;
    }

    public List<CashFlow> cashflows(Market market) {
//        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
        return null;
    }

    public static String[][] templateTradeData() {
        DigitalOption digitalOption = new DigitalOption();
        digitalOption.setStartDate(LocalDate.now());
        digitalOption.setMaturityDate(LocalDate.now().plusYears(1));
        digitalOption.setExerciseDate(LocalDate.now().plusYears(1));
        digitalOption.setStrike(1000.0);
        digitalOption.setOptionDirection(OptionDirection.Call);
//        digitalOption.setUnderlyingTicker(new JqTicker("SH000300"));
//        digitalOption.setDayCountImpl(new Act365());
        digitalOption.setDigitalPayoff(1.0);
        digitalOption.setNotional(1.0);
        digitalOption.setDomCurrency(Currency.Cny);
        digitalOption.setValuationModel(ValuationModel.Analytical);
        return Deal.templateTradeData(DigitalOption.class, digitalOption);
    }
}
