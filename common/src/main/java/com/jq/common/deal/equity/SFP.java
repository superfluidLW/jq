//package com.jq.common.deal.equity;
//
//import com.jq.common.deal.DealType;
//import com.jq.common.market.convention.*;
//import com.jq.common.output.CashFlow;
//import com.jq.common.output.Result;
//import com.jq.common.deal.Deal;
//import com.jq.impl.common.Calendar;
//import com.jq.impl.calculator.mc.MonteCarloCalculator;
//import com.jq.impl.market.JqMarket;
//import com.jq.impl.market.convention.Act365;
//import com.jq.common.market.mktObj.JqTicker;
//import com.jq.impl.util.JqConstant;
//import lombok.Data;
//
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineManager;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Data
//public class SFP extends Option {
//    public static ScriptEngineManager mgr = new ScriptEngineManager();
//    public static ScriptEngine engine=mgr.getEngineByName("JavaScript");
//    public String payOffDefinition;
//
//    public List<String> koCond=new ArrayList<>();
//    public List<String> accCond=new ArrayList<>();
//    public List<String> normalCond=new ArrayList<>();
//    public static Calendar calendar = new Calendar(Venue.Cib);
//
//    public List<String> tires=Arrays.asList(".*S[>|<].*", ".*St[>|<].*", ".*ST[>|<].*");
//    public SFP(){
//        super();
//        dealType = DealType.SFP;
//    }
//
//    @Override
//    public Option bumpMaturity(int offset) {
//        SFP vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), SFP.class);
//        vo.setMaturityDate(this.maturityDate.plusDays(offset));
//        vo.setExerciseDate(exerciseDate.plusDays(offset));
//        return vo;
//    }
//
//    @Override
//    public Double[][] payOffChart() {
//        double delta = 0.2/50;
//        Double[][] payoff = new Double[100][2];
//        int offset = payoff.length/2;
//        for(int i = 0; i < payoff.length; ++i){
//            Double price = 1.0 + (i-offset) * delta;
//
//            payoff[i][0] = price;
//            payoff[i][1] = calcPayOff(new LinkedHashMap<LocalDate, Double>(){{put(exerciseDate, price);}});
//        }
//        return payoff;
//    }
//
//    public List<LocalDate> obsDates() {
//        List<LocalDate> obsDates = new ArrayList<>();
//        for (LocalDate date = startDate; date.isBefore(exerciseDate) || date.equals(exerciseDate); date = date.plusDays(1)) {
//            if (calendar.isBizDay(date)) {
//                obsDates.add(date);
//            }
//        }
//
//        return obsDates;
//    }
//
//    @Override
//    public Double calcPayOff(LinkedHashMap<LocalDate, Double> path) {
//        try {
//            List<String> condExpr = Arrays.asList(this.payOffDefinition.split(JqConstant.semiColon));
//
//            koCond = condExpr.stream().filter(e -> e.matches(".*S[>|<].*")).collect(Collectors.toList());
//            accCond = condExpr.stream().filter(e -> e.matches(".*St[>|<].*")).collect(Collectors.toList());
//            normalCond = condExpr.stream().filter(e -> e.matches(".*ST[>|<].*")).collect(Collectors.toList());
//
//            if(koCond.size() > 0) {
//                for (int i = 0; i < koCond.size(); i++) {
//                    String condition = koCond.get(i).split(JqConstant.delim)[0];
//                    String payoff = koCond.get(i).split(JqConstant.delim)[1];
//                    for (LocalDate date : path.keySet()) {
//                        engine.put("S", path.get(date));
//                        if ((Boolean) engine.eval(condition)) {
//                            return (Double) engine.eval(payoff);
//                        }
//                    }
//                }
//            }
//
//            if(accCond.size() > 0) {
//                Double rate = 0.0;
//                List<LocalDate> obsDates = obsDates();
//                for (LocalDate date : obsDates) {
//                    for (int i = 0; i < accCond.size(); ++i) {
//                        String condition = accCond.get(i).split(JqConstant.delim)[0];
//                        String payoff = accCond.get(i).split(JqConstant.delim)[1];
//                        engine.put("St", path.get(date));
//                        if ((Boolean) engine.eval(condition)) {
//                            rate += (Double) engine.eval(payoff);
//                            break;
//                        }
//                    }
//                }
//                return rate / obsDates.size();
//            }
//
//            if(normalCond.size() > 0) {
//                for (int i = 0; i < normalCond.size(); ++i) {
//                    String condition = normalCond.get(i).split(JqConstant.delim)[0];
//                    String payoff = normalCond.get(i).split(JqConstant.delim)[1];
//                    engine.put("ST", path.get(exerciseDate));
//                    if ((Boolean) engine.eval(condition)) {
//                      if(payoff.equals("0.005+5.1*(1.0-ST > 0.04 ? 0.04 : 1.0-ST))")){
//                        int x = 1;
//                      }
//
//                        return  (Double) engine.eval(payoff);
//                    }
//                }
//        }
//            Double price = path.get(exerciseDate);
//            return optionDirection == OptionDirection.Call ? Math.max(price - strike, 0.0) : Math.max(strike - price, 0.0);
//        } catch (Exception ex) {
//
//        }
//        return 0.0;
//    }
//
//    @Override
//    public Result calc(JqMarket jqMarket) {
//        MonteCarloCalculator mc = new MonteCarloCalculator();
//        return mc.calc(this, jqMarket);
//    }
//
//    @Override
//    public List<CashFlow> cashflows(JqMarket jqMarket) {
//        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
//        return null;
//    }
//
//    public static String[][] templateTradeData() {
//        SFP sfp = new SFP();
//        sfp.setStartDate(LocalDate.now());
//        sfp.setMaturityDate(LocalDate.now().plusYears(1));
//        sfp.setExerciseDate(LocalDate.now().plusYears(1));
//        sfp.setDomCurrency(Currency.Cny);
//        sfp.setUnderlyingTicker(new JqTicker("SH000300"));
//        sfp.setDayCountImpl(new Act365());
//        sfp.setNotional(1.0);
//        sfp.setPayOffDefinition("ST>=1.05,0.02;ST>0.85,0.02+(1.05-ST)*0.25;ST<=0.85,0.07");
//        sfp.setValuationModel(ValuationModel.MonteCarlo);
//        sfp.setNumMcPath(1000);
//        sfp.setCalcMcGreeks(true);
//        return Deal.templateTradeData(SFP.class, sfp);
//    }
//
//    @Override
//    public List<CashFlow> calcPayoff() {
//        return null;
//    }
//}
