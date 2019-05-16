package com.chendu.jq.core.equity.calculator.mc;

import com.chendu.jq.core.equity.Option;
import com.chendu.jq.core.market.JqMarket;
import javafx.util.Pair;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.time.LocalDate;
import java.util.*;

public class PricePath {
    public static NormalDistribution normal = new NormalDistribution(0.0, 1.0);

    public static List<LinkedHashMap<LocalDate, Double>> genPath(Option option, JqMarket jqMarket){
        List<LinkedHashMap<LocalDate, Double>> paths = new ArrayList<>(option.numMcPath);
        Double s0 = jqMarket.tickerPrice(option.getUnderlyingTicker());
        Double vol = jqMarket.tickerVol(option.getUnderlyingTicker());
        Double maturityTime = option.getDayCount().yearFraction(jqMarket.getMktDate(), option.getMaturityDate());
        Double q = jqMarket.getDividendCurveMap().get(option.getUnderlyingTicker()).getZeroRate(maturityTime);
        Double r = jqMarket.jqCurve(option.getDomCurrency()).getZeroRate(maturityTime);

        for(int i = 0; i < option.numMcPath/2; ++i){
            List<LinkedHashMap<LocalDate, Double>> path = genSinglePath(s0, r, q, vol, option, jqMarket);

            paths.addAll(path);
        }

        return paths;
    }

    private static List<LinkedHashMap<LocalDate, Double>> genSinglePath(Double s0, Double r, Double q, Double vol, Option option, JqMarket jqMarket){
        LinkedHashMap<LocalDate, Double> path = new LinkedHashMap<>();
        LinkedHashMap<LocalDate, Double> antitheticPath = new LinkedHashMap<>();

        LocalDate curDate = LocalDate.from(jqMarket.getMktDate());
        Double s = s0;
        Double sAnti = s0;
        path.put(curDate, s);
        antitheticPath.put(curDate, sAnti);
        LocalDate nextDate = curDate.plusDays(1);
        Double mu = r - q - vol*vol/2.0;
        while (!nextDate.isAfter(option.getMaturityDate())){
            Double dt = option.getDayCount().yearFraction(curDate, nextDate);
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
