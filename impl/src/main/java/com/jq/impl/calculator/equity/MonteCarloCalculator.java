package com.jq.impl.calculator.equity;

import com.jq.common.deal.Deal;
import com.jq.common.deal.equity.Option;
import com.jq.common.market.Market;
import com.jq.impl.convention.DayCountImpl;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

public class MonteCarloCalculator extends OptionCalculator {
    @Override
    public Double calcPv(Deal trade, Market market) {
        Option option = (Option)trade;
        List<LinkedHashMap<LocalDate, Double>> paths = PricePath.genPath(option, market);

        Double t = DayCountImpl.get(option.getDayCount()).yearFraction(market.getMktDate(), option.getMaturityDate());
        Double df = market.getYieldCurveMap().get(option.getDomCurrency()).getDf(t);

        return df * paths.stream().mapToDouble(e -> option.calcPayOff(e)).average().getAsDouble();
    }
}
