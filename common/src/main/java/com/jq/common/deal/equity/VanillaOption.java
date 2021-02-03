package com.jq.common.deal.equity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jq.common.convention.*;
import com.jq.common.deal.Deal;
import com.jq.common.deal.DealType;
import com.jq.common.market.Market;
import com.jq.common.market.Security;
import com.jq.common.output.CashFlow;
import com.jq.common.output.Result;
import lombok.Data;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class VanillaOption extends Option {
    public VanillaOption(){
        super();
        dealType = DealType.VanillaOption;
    }

    @Override
    public Option bumpMaturity(int offset) {
//        VanillaOption vo = JsonMapper.readValue(JsonMapper.writeValueAsString(this), VanillaOption.class);
//        vo.setMaturityDate(this.maturityDate.plusDays(offset));
//        vo.setExerciseDate(exerciseDate.plusDays(offset));
        return null;
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

    public Result calc(Market market) {
//        if(valuationModel == ValuationModel.MonteCarlo){
//            MonteCarloCalculator mc = new MonteCarloCalculator();
//            return mc.calc(this, jqMarket);
//        }
//        else{
//            EuropeanVanillaCalculator evc = new EuropeanVanillaCalculator();
//            return evc.calc(this, jqMarket);
//        }
        return null;
    }

    public List<CashFlow> cashflows(Market market) {
//        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
        return null;
    }

    public static VanillaOption sampleDeal() {
        VanillaOption vanillaOption = new VanillaOption();
        vanillaOption.setStartDate(LocalDate.now());
        vanillaOption.setMaturityDate(LocalDate.now().plusYears(1));
        vanillaOption.setExerciseDate(LocalDate.now().plusYears(1));
        vanillaOption.setStrike(1000.0);
        vanillaOption.setOptionDirection(OptionDirection.Call);
        vanillaOption.setUnderlying(new Security("SH000300", Venue.ShExg));
        vanillaOption.setDayCount(DayCount.Act365);
        vanillaOption.setNotional(1.0);
        vanillaOption.setDomCurrency(Currency.Cny);
        vanillaOption.setValuationModel(ValuationModel.Analytical);
        return vanillaOption;
    }

    @Override
    public List<CashFlow> calcPayoff() {
        return null;
    }
}
