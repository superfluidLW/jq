package com.chendu.jq.core.market;

import com.chendu.jq.core.common.jqEnum.Currency;
import com.chendu.jq.core.market.mktObj.TickerInfo;
import com.chendu.jq.core.market.mktObj.Volatility;
import com.chendu.jq.core.market.mktObj.YieldCurve;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

@Data
public class JqMarket implements Serializable {
    public JqMarket(String[][] table){
    }

    private LocalDate mktDate;
    private Map<Currency, YieldCurve> yieldCurveMap;
    private Map<JqTicker, YieldCurve> dividendCurveMap;
    private Map<JqTicker, TickerInfo> tickerMap;
    private Map<JqTicker, Volatility> volatilityMap;

    public Double tickerPrice(JqTicker ticker){
        return tickerMap.get(ticker).getPrice();
    }

    
}
