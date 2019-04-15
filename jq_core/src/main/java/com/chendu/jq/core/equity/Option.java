package com.chendu.jq.core.equity;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.jqEnum.OptionDirection;
import com.chendu.jq.core.common.jqEnum.ValuationModel;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.MktAction;
import com.chendu.jq.core.market.mktObj.JqTicker;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Data
public abstract class Option extends JqTrade {
    public List<LocalDate> exerciseDates;
    public JqTicker underlyingTicker;
    public ValuationModel valuationModel;
    public OptionDirection optionDirection;

    public Option(){
        super();
    }

    public List<MktAction> bumpSpot(Double bump){
        return Arrays.asList(new MktAction(JqMarket::getTickerMap, underlyingTicker, bump));
    }

    protected abstract List<JqCashflow> calcPayoff();
}
