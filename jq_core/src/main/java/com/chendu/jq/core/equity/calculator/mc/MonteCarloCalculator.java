package com.chendu.jq.core.equity.calculator.mc;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.equity.Option;
import com.chendu.jq.core.equity.calculator.OptionCalculator;
import com.chendu.jq.core.market.JqMarket;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

public class MonteCarloCalculator extends OptionCalculator {
    @Override
    public Double calcPv(JqTrade trade, JqMarket jqMarket) {
        Option option = (Option)trade;
        List<LinkedHashMap<LocalDate, Double>> paths = PricePath.genPath(option, jqMarket);

        Double t = option.getDayCount().yearFraction(jqMarket.getMktDate(), option.getMaturityDate());
        Double df = jqMarket.getYieldCurveMap().get(option.getDomCurrency()).getDf(t);

        return df * paths.stream().mapToDouble(e -> option.calcPayOff(e)).average().getAsDouble();
    }
}
