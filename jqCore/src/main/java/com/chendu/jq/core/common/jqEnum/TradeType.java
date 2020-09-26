package com.chendu.jq.core.common.jqEnum;

import lombok.Getter;

public enum TradeType {
    Deposit("存款"),
    VanillaOption("香草期权"),
    DigitalOption("二值期权"),
    DoubleBarrierOption("双障碍期权"),
    RangeAccrual("区间累计期权"),
    SFP("结构性理财产品");

    @Getter
    private String nameCn;

    TradeType(String nameCn){
        this.nameCn = nameCn;
    }
}
