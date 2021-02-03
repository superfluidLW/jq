package com.jq.common.output;

import lombok.Getter;

public enum ReqResult {
    PV("估值"),
    ModifiedDuration("修正久期"),
    Convexity("凸性");


    @Getter
    private String description;

    ReqResult(String description){
        this.description = description;
    }
}
