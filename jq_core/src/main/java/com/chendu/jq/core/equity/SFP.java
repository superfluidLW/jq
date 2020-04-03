package com.chendu.jq.core.equity;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCalendar;
import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.dayCount.DayCount;
import com.chendu.jq.core.common.jqEnum.*;
import com.chendu.jq.core.equity.calculator.analytical.EuropeanVanillaCalculator;
import com.chendu.jq.core.equity.calculator.mc.MonteCarloCalculator;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.mktObj.JqTicker;
import com.chendu.jq.core.util.JqConstant;
import com.chendu.jq.core.util.JsonUtils;
import lombok.Data;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SFP extends Option {
    public static ScriptEngineManager mgr = new ScriptEngineManager();
    public static ScriptEngine engine=mgr.getEngineByName("JavaScript");
    public String payOffDefinition;

    public List<String> koCond=new ArrayList<>();
    public List<String> accCond=new ArrayList<>();
    public List<String> normalCond=new ArrayList<>();
    public static JqCalendar jqCalendar = new JqCalendar(Venue.Cib);

    public List<String> tires=Arrays.asList(".*S[>|<].*", ".*St[>|<].*", ".*ST[>|<].*");
    public SFP(){
        super();
        tradeType = TradeType.SFP;
    }

    @Override
    public Option bumpMaturity(int offset) {
        SFP vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), SFP.class);
        vo.setMaturityDate(this.maturityDate.plusDays(offset));
        vo.setExerciseDate(exerciseDate.plusDays(offset));
        return vo;
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

    public List<LocalDate> obsDates() {
        List<LocalDate> obsDates = new ArrayList<>();
        for (LocalDate date = startDate; date.isBefore(exerciseDate) || date.equals(exerciseDate); date = date.plusDays(1)) {
            if (jqCalendar.isBizDay(date)) {
                obsDates.add(date);
            }
        }

        return obsDates;
    }

    @Override
    public Double calcPayOff(LinkedHashMap<LocalDate, Double> path) {
        try {
            List<String> condExpr = Arrays.asList(this.payOffDefinition.split(JqConstant.semiColon));

            koCond = condExpr.stream().filter(e -> e.matches(".*S[>|<].*")).collect(Collectors.toList());
            accCond = condExpr.stream().filter(e -> e.matches(".*St[>|<].*")).collect(Collectors.toList());
            normalCond = condExpr.stream().filter(e -> e.matches(".*ST[>|<].*")).collect(Collectors.toList());

            if(koCond.size() > 0) {
                for (int i = 0; i < koCond.size(); i++) {
                    String condition = koCond.get(i).split(JqConstant.delim)[0];
                    String payoff = koCond.get(i).split(JqConstant.delim)[1];
                    for (LocalDate date : path.keySet()) {
                        engine.put("S", path.get(date));
                        if ((Boolean) engine.eval(condition)) {
                            return (Double) engine.eval(payoff);
                        }
                    }
                }
            }

            if(accCond.size() > 0) {
                Double rate = 0.0;
                List<LocalDate> obsDates = obsDates();
                for (LocalDate date : obsDates) {
                    for (int i = 0; i < accCond.size(); ++i) {
                        String condition = accCond.get(i).split(JqConstant.delim)[0];
                        String payoff = accCond.get(i).split(JqConstant.delim)[1];
                        engine.put("St", path.get(date));
                        if ((Boolean) engine.eval(condition)) {
                            rate += (Double) engine.eval(payoff);
                            break;
                        }
                    }
                }
                return rate / obsDates.size();
            }

            if(normalCond.size() > 0) {
                for (int i = 0; i < normalCond.size(); ++i) {
                    String condition = normalCond.get(i).split(JqConstant.delim)[0];
                    String payoff = normalCond.get(i).split(JqConstant.delim)[1];
                    engine.put("ST", path.get(exerciseDate));
                    if ((Boolean) engine.eval(condition)) {
                      if(payoff.equals("0.005+5.1*(1.0-ST > 0.04 ? 0.04 : 1.0-ST))")){
                        int x = 1;
                      }

                        return  (Double) engine.eval(payoff);
                    }
                }
        }
            Double price = path.get(exerciseDate);
            return optionDirection == OptionDirection.Call ? Math.max(price - strike, 0.0) : Math.max(strike - price, 0.0);
        } catch (Exception ex) {

        }
        return 0.0;
    }

    @Override
    public JqResult calc(JqMarket jqMarket) {
        MonteCarloCalculator mc = new MonteCarloCalculator();
        return mc.calc(this, jqMarket);
    }

    @Override
    public List<JqCashflow> cashflows(JqMarket jqMarket) {
        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
        return null;
    }

    public static String[][] templateTradeData() {
        SFP sfp = new SFP();
        sfp.setStartDate(LocalDate.now());
        sfp.setMaturityDate(LocalDate.now().plusYears(1));
        sfp.setExerciseDate(LocalDate.now().plusYears(1));
        sfp.setStrike(1000.0);
        sfp.setUnderlyingTicker(new JqTicker("SH000300"));
        sfp.setDayCount(new DayCount(DayCountType.Act365));
        sfp.setNotional(1.0);
        sfp.setPayOffDefinition("S>1.05,S-1.05;S<=1.05,0");
        return JqTrade.templateTradeData(SFP.class, sfp);
    }

    @Override
    public List<JqCashflow> calcPayoff() {
        return null;
    }
}
