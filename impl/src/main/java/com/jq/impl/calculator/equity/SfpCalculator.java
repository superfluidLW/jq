//package com.jq.impl.calculator.equity;
//
//import com.jq.common.deal.Deal;
//import com.jq.common.deal.equity.SFP;
//import com.jq.common.market.Market;
//
//import java.time.LocalDate;
//import java.util.LinkedHashMap;
//import java.util.List;
//
//public class SfpCalculator extends OptionCalculator {
//    @Override
//    public Double calcPv(Deal trade, Market market) {
//        SFP sfp = (SFP)trade;
//        List<LinkedHashMap<LocalDate, Double>> paths = PricePath.genPath(sfp, market);
//        Double t = sfp.getDayCountImpl().yearFraction(market.getMktDate(), sfp.getMaturityDate());
//        Double df = market.getYieldCurveMap().get(sfp.getDomCurrency()).getDf(t);
//
//        return df * paths.stream().mapToDouble(e -> sfp.calcPayOff(e)).average().getAsDouble();
//    }
//}
