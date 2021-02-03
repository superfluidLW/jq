package com.jq.common.deal.equity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jq.common.market.Security;
import com.jq.common.output.CashFlow;
import com.jq.common.deal.Deal;
import com.jq.common.convention.OptionDirection;
import com.jq.common.convention.ValuationModel;
import lombok.Data;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Option extends Deal {
    protected Double strike;
    protected LocalDate exerciseDate;
    protected Security underlying;
    protected ValuationModel valuationModel;
    protected Integer numMcPath;
    protected Boolean calcMcGreeks;
    protected OptionDirection optionDirection;

    public Option(){
        super();
        numMcPath = 5000;
        calcMcGreeks = false;
    }

    public abstract Option bumpMaturity(int offset);

    public abstract Double[][] payOffChart();

//    public List<MktAction> bumpSpot(Double bump){
//        return Arrays.asList(new MktAction(JqMarket.tickerField(), underlyingTicker, bump));
//    }
//
//    public List<MktAction> bumpVol(Double bump){
//        return Arrays.asList(new MktAction(JqMarket.volatilityField(), underlyingTicker, bump));
//    }
//
//    public List<MktAction> bumpYieldCurve(Double bump){
//        return Arrays.asList(new MktAction(JqMarket.yieldCurveField(), domCurrency, bump));
//    }

    public abstract Double calcPayOff(LinkedHashMap<LocalDate, Double> path);
    public abstract List<CashFlow> calcPayoff();
}
