package com.chendu.jq.core.equity;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.jqEnum.OptionDirection;
import com.chendu.jq.core.common.jqEnum.ValuationModel;
import com.chendu.jq.core.market.JqSymbol;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public abstract class Option extends JqTrade {
    public List<LocalDate> exerciseDates;
    public JqSymbol underlyingAssetSymbol;
    public ValuationModel valuationModel;
    public OptionDirection optionDirection;

    public Option(){
        super();
    }

    protected abstract List<JqCashflow> calcPayoff();
}
