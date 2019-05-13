package com.chendu.jq.core.equity.calculator.mc;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.equity.Option;
import com.chendu.jq.core.equity.calculator.OptionCalculator;
import com.chendu.jq.core.market.JqMarket;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.List;

public class MonteCarloCalculator extends OptionCalculator {
    @Override
    public Double calcPv(JqTrade trade, JqMarket jqMarket) {
        Option option = (Option)trade;
        List<List<Pair<LocalDate, Double>>> paths = PricePath.genPath(option, jqMarket);
        return paths.stream().mapToDouble(e -> option.calcPayOff(e)).average().getAsDouble();
    }
}
