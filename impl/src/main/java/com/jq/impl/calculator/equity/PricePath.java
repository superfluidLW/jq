package com.jq.impl.calculator.equity;

import com.jq.common.deal.equity.Option;
import com.jq.common.market.Market;
import com.jq.impl.convention.DayCountImpl;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class PricePath {
    public static NormalDistribution normal = new NormalDistribution(0.0, 1.0);

    public static List<LinkedHashMap<LocalDate, Double>> genPath(Option option, Market market){
        List<LinkedHashMap<LocalDate, Double>> paths = new ArrayList<>(option.getNumMcPath());
        Double s0 = market.tickerPrice(option.getUnderlying());
        Double vol = market.tickerVol(option.getUnderlying());
        Double maturityTime = DayCountImpl.get(option.getDayCount()).yearFraction(market.getMktDate(), option.getMaturityDate());
        Double q = market.getDividendCurveMap().get(option.getUnderlying()).getZeroRate(maturityTime);
        Double r = market.jqCurve(option.getDomCurrency()).getZeroRate(maturityTime);

        for(int i = 0; i < option.getNumMcPath()/2; ++i){
            List<LinkedHashMap<LocalDate, Double>> path = genSinglePath(s0, r, q, vol, option, market);

            paths.addAll(path);
        }

        return paths;
    }

    private static List<LinkedHashMap<LocalDate, Double>> genSinglePath(Double s0, Double r, Double q, Double vol, Option option, Market market){
        LinkedHashMap<LocalDate, Double> path = new LinkedHashMap<>();
        LinkedHashMap<LocalDate, Double> antitheticPath = new LinkedHashMap<>();

        LocalDate curDate = LocalDate.from(market.getMktDate());
        Double s = s0;
        Double sAnti = s0;
        path.put(curDate, s);
        antitheticPath.put(curDate, sAnti);
        LocalDate nextDate = curDate.plusDays(1);
        Double mu = r - q - vol*vol/2.0;
        while (!nextDate.isAfter(option.getMaturityDate())){
            Double dt = DayCountImpl.get(option.getDayCount()).yearFraction(curDate, nextDate);
            Double dw = normal.sample();
            s = s * Math.exp (mu * dt + vol * normal.sample() * Math.sqrt(dt));
            sAnti = sAnti * Math.exp (mu * dt + vol * normal.sample() * Math.sqrt(dt));
            path.put(nextDate, s);
            antitheticPath.put(nextDate, sAnti);
            curDate = nextDate;
            nextDate = curDate.plusDays(1);
        }

        return Arrays.asList(path, antitheticPath);
    }
}
