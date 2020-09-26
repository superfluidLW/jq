package com.chendu.jq.core.equity.calculator.mc;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.equity.Option;
import com.chendu.jq.core.equity.SFP;
import com.chendu.jq.core.equity.calculator.OptionCalculator;
import com.chendu.jq.core.market.JqMarket;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

public class SfpCalculator extends OptionCalculator {
    @Override
    public Double calcPv(JqTrade trade, JqMarket jqMarket) {
        SFP sfp = (SFP)trade;
        List<LinkedHashMap<LocalDate, Double>> paths = PricePath.genPath(sfp, jqMarket);
        Double t = sfp.getDayCount().yearFraction(jqMarket.getMktDate(), sfp.getMaturityDate());
        Double df = jqMarket.getYieldCurveMap().get(sfp.getDomCurrency()).getDf(t);

        return df * paths.stream().mapToDouble(e -> sfp.calcPayOff(e)).average().getAsDouble();
    }
}
