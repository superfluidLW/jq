package com.jq.common.market;

import com.jq.common.convention.Currency;
import com.jq.common.deal.Deal;
import com.jq.common.deal.equity.Option;
import com.jq.common.market.curve.SwapCurve;
import com.jq.common.market.quote.SecurityQuote;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Market implements Serializable {
    public LocalDate mktDate;
    public Map<Currency, SwapCurve> yieldCurveMap = new HashMap<>();
    public Map<Security, SwapCurve> dividendCurveMap = new HashMap<>();
    public Map<Security, SecurityQuote> securityQuoteMap = new HashMap<>();
    public Map<Security, Volatility> volatilityMap = new HashMap<>();

    public Map<String, Map<Object, MktAction>> actions = new HashMap<>();

    public SwapCurve jqCurve(Currency currency){
        SwapCurve jqCurve = yieldCurveMap.get(currency);
        if(actions.containsKey("yieldCurveMap")){
            if(actions.get("yieldCurveMap").containsKey(currency)){
                return jqCurve.bump(actions.get("yieldCurveMap").get(currency).bumpValue);
            }
        }
        return jqCurve;
    }

    public List<MktAction> bumpSecuritySpot(Security underlying, Double bump){
        return Arrays.asList(new MktAction("securityQuoteMap", underlying, bump));
    }

    public List<MktAction> bumpVol(Security underlying, Double bump){
        return Arrays.asList(new MktAction("volatilityMap", underlying, bump));
    }

    public List<MktAction> bumpYieldCurve(Currency currency, Double bump){
        return Arrays.asList(new MktAction("yieldCurveMap", currency, bump));
    }

    public Double tickerVol(Security ticker){
        Double vol = volatilityMap.get(ticker).getVol();
        if(actions.containsKey("volatilityMap")){
            if(actions.get("volatilityMap").containsKey(ticker)){
                vol += actions.get("volatilityMap").get(ticker).bumpValue;
            }
        }
        return vol;
    }

    public Double tickerPrice(Security security) {
        Double price = securityQuoteMap.get(security).getQuote();
        if(actions.containsKey("securityQuoteMap")){
            if(actions.get("securityQuoteMap").containsKey(security)){
                price += actions.get("securityQuoteMap").get(security).bumpValue;
            }
        }
        return price;
    }

    public void updateActions(List<MktAction> newActions) {
        Map<String, List<MktAction>> groups = newActions.stream().collect(Collectors.groupingBy(e -> e.field));
        actions = new HashMap<>();
        for (String field : groups.keySet()
                ) {
            actions.put(field, new HashMap<>());
            for (MktAction mktAction : groups.get(field)) {
                actions.get(field).put(mktAction.mktObj, mktAction);
            }
        }
    }
}
