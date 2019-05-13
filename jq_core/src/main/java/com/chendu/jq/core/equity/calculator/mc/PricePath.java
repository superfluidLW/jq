package com.chendu.jq.core.equity.calculator.mc;

import com.chendu.jq.core.equity.Option;
import com.chendu.jq.core.market.JqMarket;
import javafx.util.Pair;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PricePath {
    public static NormalDistribution normal = new NormalDistribution(0.0, 1.0);

    public static List<List<Pair<LocalDate, Double>>> genPath(Option option, JqMarket jqMarket){
        List<List<Pair<LocalDate, Double>>> paths = new ArrayList<>();
        Double s0 = jqMarket.tickerPrice(option.getUnderlyingTicker());
        Double vol = jqMarket.tickerVol(option.getUnderlyingTicker());
        Double maturityTime = option.getDayCount().yearFraction(jqMarket.getMktDate(), option.getMaturityDate());
        Double q = jqMarket.getDividendCurveMap().get(option.getUnderlyingTicker()).getDf(maturityTime);
        Double r = jqMarket.jqCurve(option.getDomCurrency()).getDf(maturityTime);

        for(int i = 0; i < option.numPath/2; ++i){
            List<Pair<LocalDate, Double>> path = genSinglePath(s0, r, q, vol, option, jqMarket);

            paths.add(path);
            paths.add(genAntitheticPath(path));
        }

        return paths;
    }

    private static List<Pair<LocalDate, Double>> genSinglePath(Double s0, Double r, Double q, Double vol, Option option, JqMarket jqMarket){
        List<Pair<LocalDate, Double>> path = new ArrayList<>();

        LocalDate curDate = LocalDate.from(jqMarket.getMktDate());
        path.add(new Pair<>(curDate, s0));
        LocalDate nextDate = curDate.plusDays(1);
        while (!nextDate.isAfter(option.getMaturityDate())){
            Double dt = option.getDayCount().yearFraction(curDate, nextDate);
            s0 *= (1.0 + (r-q) * dt + vol * normal.sample() * Math.sqrt(dt));
            curDate = nextDate;
            nextDate = curDate.plusDays(1);
        }

        return path;
    }

    private static List<Pair<LocalDate, Double>> genAntitheticPath(List<Pair<LocalDate, Double>> path){
        List<Pair<LocalDate, Double>> antitheticPath = new ArrayList<>();

        for(int i = 0; i < path.size(); ++i){
            antitheticPath.add(new Pair<>(path.get(i).getKey(), -path.get(i).getValue()));
        }
        return antitheticPath;
    }
}
