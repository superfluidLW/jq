package com.chendu.jq.core.common.jqEnum;

import lombok.Getter;

public enum   TradeLabel {
    TradeType("产品类型"),
    StartDate("开始日期"),
    MaturityDate("到期日期"),
    StrikeDate("行权日期"),
    underlyingAssetSymbol("标的资产编码"),
    ValuationModel("估值方法"),
    Strike("行权价格"),
    DayCount("计息基准"),
    OptionDirection("期权方向"),
    DomCurrency("本币币种"),
    FgnCurrency("外币币种"),
    exerciseDates("行权日期");

    @Getter
    private String nameCn;

    TradeLabel(String nameCn){
        this.nameCn = nameCn;
    }
}
