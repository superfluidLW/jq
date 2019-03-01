package com.chendu.jq.core.common.jqEnum;

import lombok.Getter;

public enum ValuationModel {
    Analytical("解析"),
    MonteCarlo("蒙特卡洛");

    @Getter
    private String nameCn;

    ValuationModel(String nameCn){
        this.nameCn = nameCn;
    }
}
