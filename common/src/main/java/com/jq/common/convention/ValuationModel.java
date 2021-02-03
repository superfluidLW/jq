package com.jq.common.convention;

import lombok.Getter;

public enum ValuationModel {
    Analytical("解析"),
    MonteCarlo("蒙特卡洛");

    @Getter
    private String description;

    ValuationModel(String description){
        this.description = description;
    }
}
