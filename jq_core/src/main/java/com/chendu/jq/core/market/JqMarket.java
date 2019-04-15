package com.chendu.jq.core.market;

import com.chendu.jq.core.common.jqEnum.Currency;
import com.chendu.jq.core.market.mktObj.JqTicker;
import com.chendu.jq.core.market.mktObj.JqTickerInfo;
import com.chendu.jq.core.market.mktObj.JqVol;
import com.chendu.jq.core.market.mktObj.JqCurve;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class JqMarket implements Serializable {
    private LocalDate mktDate;
    private Map<Currency, JqCurve> yieldCurveMap;
    private Map<JqTicker, JqCurve> dividendCurveMap;
    private Map<JqTicker, JqTickerInfo> tickerMap;
    private Map<JqTicker, JqVol> volatilityMap;

    private Map<Function<JqMarket, Object>, Map<MktObj, MktAction>> actions;

    public Double tickerPrice(JqTicker ticker) {
        return tickerMap.get(ticker).getPrice();
    }

    public void updateActions(List<MktAction> newActions) {
        Map<Function<JqMarket, Object>, List<MktAction>> groups = newActions.stream().collect(Collectors.groupingBy(e -> e.func));
        actions = new HashMap<>();
        for (Function<JqMarket, Object> func : groups.keySet()
                ) {
            actions.put(func, new HashMap<>());
            for (MktAction mktAction : groups.get(func)) {
                actions.get(func).put(mktAction.mktObj, mktAction);
            }
        }
    }
}
