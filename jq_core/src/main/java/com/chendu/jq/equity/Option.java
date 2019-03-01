package com.chendu.jq.equity;

import com.chendu.jq.JqTrade;
import com.chendu.jq.UnderlyingAsset;
import com.chendu.jq.common.JqCashflow;
import com.chendu.jq.common.jqEnum.OptionDirection;
import com.chendu.jq.common.jqEnum.ValuationModel;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public abstract class Option extends JqTrade {
    public LocalDate strikeDate;
    public String underlyingAssetSymbol;
    public ValuationModel valuationModel;
    public OptionDirection optionDirection;

    public Option(){
        super();
    }

    protected abstract List<JqCashflow> calcPayoff();
}
