package com.chendu.jq.core.common.jqEnum;

import lombok.Getter;

public enum MktDataType {
    MktDate("估值日期"),
    RiskfreeRate("无风险利率"),
    DividendRate("分红率"),
    S0("标的现价"),
    Vol("波动率");


    @Getter
    private String nameCn;

    MktDataType(String nameCn){
        this.nameCn = nameCn;
    }
}
