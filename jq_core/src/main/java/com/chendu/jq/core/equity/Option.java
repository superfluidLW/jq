package com.chendu.jq.core.equity;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.jqEnum.OptionDirection;
import com.chendu.jq.core.common.jqEnum.ValuationModel;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.MktAction;
import com.chendu.jq.core.market.mktObj.JqTicker;
import javafx.util.Pair;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedHashMap;

@Data
public abstract class Option extends JqTrade {
    public Double strike;
    public List<LocalDate> observeDates;
    public List<LocalDate> exerciseDates;
    public JqTicker underlyingTicker;
    public ValuationModel valuationModel;
    public Integer numPath;
    public Boolean calcGreeks;
    public OptionDirection optionDirection;

    public Option(){
        super();
        numPath = 1000;
        calcGreeks = false;
    }

    public abstract Option bumpMaturity(int offset);

    public abstract Double[][] payOffChart();

    public List<MktAction> bumpSpot(Double bump){
        return Arrays.asList(new MktAction(JqMarket.tickerField(), underlyingTicker, bump));
    }

    public List<MktAction> bumpVol(Double bump){
        return Arrays.asList(new MktAction(JqMarket.volatilityField(), underlyingTicker, bump));
    }

    public List<MktAction> bumpYieldCurve(Double bump){
        return Arrays.asList(new MktAction(JqMarket.yieldCurveField(), domCurrency, bump));
    }

    public abstract Double calcPayOff(LinkedHashMap<LocalDate, Double> path);
    public abstract List<JqCashflow> calcPayoff();
}
