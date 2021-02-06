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
public class DoubleBarrierOption extends Option {
    public Double koRebate;
    public Double kiCoupon;
    public Double lBarrier;
    public Double uBarrier;
    public Double participationRate;

    public DoubleBarrierOption(){
        super();
        dealType = DealType.DoubleBarrierOption;
    }

    @Override
    public Option bumpMaturity(int offset) {
//        DoubleBarrierOption vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), DoubleBarrierOption.class);
//        vo.setMaturityDate(this.maturityDate.plusDays(offset));
//        vo.setExerciseDate(this.exerciseDate.plusDays(offset));
//        return vo;
        return null;
    }

    @Override
    public Double[][] payOffChart() {
        double delta = 2*(uBarrier-lBarrier)/40.0;
        Double[][] payoff = new Double[40][2];
        int offset = payoff.length/2;
        for(int i = 0; i < payoff.length; ++i){
            Double price = strike + (i-offset) * delta;
            Double po;
            if(price > uBarrier || price < lBarrier){
                po = koRebate;
            }
            else{
                po = optionDirection == OptionDirection.Call ? Math.max(price-strike, 0) : Math.max(strike-price, 0);
                po = po * participationRate + kiCoupon;
            }
            payoff[i][0] = price;
            payoff[i][1] = po;
        }
        return payoff;
    }

    @Override
    public Double calcPayOff(LinkedHashMap<LocalDate, Double> path) {
        return null;
    }

    public Result calc(Market market) {
        Result result = new Result();

//        if(valuationModel == ValuationModel.MonteCarlo){
//            MonteCarloDKoCalculator mc = new MonteCarloDKoCalculator(300, 10000);
//            return mc.calc(this, jqMarket);
//        }
//        else if(valuationModel == ValuationModel.Analytical){
//            DoubleBarrierCalculator dbc = new DoubleBarrierCalculator();
//            return dbc.calc(this, jqMarket);
//        }

        return result;
    }

    public List<CashFlow> cashflows(Market market) {
//        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
        return null;
    }

    public static String[][] templateTradeData() {
        DoubleBarrierOption doubleBarrierOption = new DoubleBarrierOption();
        doubleBarrierOption.setStartDate(LocalDate.now());
        doubleBarrierOption.setMaturityDate(LocalDate.now().plusYears(1));
        doubleBarrierOption.setExerciseDate(LocalDate.now().plusYears(1));
        doubleBarrierOption.setOptionDirection(OptionDirection.Call);
//        doubleBarrierOption.setUnderlyingTicker(new JqTicker("SH000300"));
//        doubleBarrierOption.setDayCountImpl(new Act365());
//        doubleBarrierOption.setNotional(1000000.0);
        doubleBarrierOption.setDomCurrency(Currency.Cny);
        doubleBarrierOption.setLBarrier(0.875);
        doubleBarrierOption.setUBarrier(1.035);
        doubleBarrierOption.setStrike(0.95);
        doubleBarrierOption.setKoRebate(0.025);
        doubleBarrierOption.setKiCoupon(0.03);
        doubleBarrierOption.setParticipationRate(1.00);
        doubleBarrierOption.setValuationModel(ValuationModel.Analytical);

        return Deal.templateTradeData(DoubleBarrierOption.class, doubleBarrierOption);
    }

}
