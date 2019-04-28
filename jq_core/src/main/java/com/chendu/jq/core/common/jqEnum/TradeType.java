package com.chendu.jq.core.common.jqEnum;

import lombok.Getter;

public enum TradeType {
    VanillaOption("香草期权"),
    DigitalOption("二值期权"),
    AsianOption("亚式期权"),
    DoubleBarrierOption("双障碍期权"),
    KnockOutOption("敲出期权"),
    RangeAccrual("区间累计期权");

    @Getter
    private String nameCn;

    TradeType(String nameCn){
        this.nameCn = nameCn;
    }
}
