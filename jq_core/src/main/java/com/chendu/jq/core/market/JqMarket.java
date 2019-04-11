package com.chendu.jq.core.market;

import com.chendu.jq.core.common.jqEnum.Currency;
import com.chendu.jq.core.yieldCurve.YieldCurve;
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
    private Map<JqSymbol, YieldCurve> dividendCurveMap;
    private Map<JqSymbol, Double> quoteMap;
    private Map<JqSymbol, Double> volatilityMap;
}
