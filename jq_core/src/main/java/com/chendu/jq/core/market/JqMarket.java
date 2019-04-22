package com.chendu.jq.core.market;

import com.chendu.jq.core.common.jqEnum.Currency;
import com.chendu.jq.core.market.mktObj.JqCurve;
import com.chendu.jq.core.market.mktObj.JqTicker;
import com.chendu.jq.core.market.mktObj.JqTickerInfo;
import com.chendu.jq.core.market.mktObj.JqVol;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class JqMarket implements Serializable {
    public LocalDate mktDate;
    public Map<Currency, JqCurve> yieldCurveMap = new HashMap<>();
    public Map<JqTicker, JqCurve> dividendCurveMap = new HashMap<>();
    public Map<JqTicker, JqTickerInfo> tickerMap = new HashMap<>();
    public Map<JqTicker, JqVol> volatilityMap = new HashMap<>();

    public Map<String, Map<Object, MktAction>> actions = new HashMap<>();

    public JqCurve jqCurve(Currency currency){
        JqCurve jqCurve = yieldCurveMap.get(currency);
        if(actions.containsKey("yieldCurveMap")){
            if(actions.get("yieldCurveMap").containsKey(currency)){
                return jqCurve.bump(actions.get("volatilityMap").get(currency).bumpValue);
            }
        }
        return jqCurve;
    }

    public Double tickerVol(JqTicker ticker){
        Double vol = volatilityMap.get(ticker).getVol();
        if(actions.containsKey("volatilityMap")){
            if(actions.get("volatilityMap").containsKey(ticker)){
                vol += actions.get("volatilityMap").get(ticker).bumpValue;
            }
        }
        return vol;
    }

    public Double tickerPrice(JqTicker ticker) {
        Double price = tickerMap.get(ticker).getPrice();
        if(actions.containsKey("tickerMap")){
            if(actions.get("tickerMap").containsKey(ticker)){
                price += actions.get("tickerMap").get(ticker).bumpValue;
            }
        }
        return price;
    }

    public void updateActions(List<MktAction> newActions) {
        Map<String, List<MktAction>> groups = newActions.stream().collect(Collectors.groupingBy(e -> e.jqMktField.getName()));
        actions = new HashMap<>();
        for (String field : groups.keySet()
                ) {
            actions.put(field, new HashMap<>());
            for (MktAction mktAction : groups.get(field)) {
                actions.get(field).put(mktAction.mktObj, mktAction);
            }
        }
    }

    public static Field tickerField(){
        try {
            return JqMarket.class.getField("tickerMap");
        }
        catch (Exception ex){
            System.out.println("Get field failed!");
            return null;
        }
    }

    public static Field yieldCurveField(){
        try {
            return JqMarket.class.getField("yieldCurveMap");
        }
        catch (Exception ex){
            System.out.println("Get field failed!");
            return null;
        }
    }

    public static Field volatilityField(){
        try {
            return JqMarket.class.getField("volatilityMap");
        }
        catch (Exception ex){
            System.out.println("Get field failed!");
            return null;
        }
    }
}
