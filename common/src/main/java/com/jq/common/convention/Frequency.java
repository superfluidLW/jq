package com.jq.common.convention;

import lombok.Getter;

public enum Frequency {
    Continuous("连续"),
    Monthly("每月"),
    Quarterly("每季度"),
    SemiAnnual("每半年"),
    Annually("每年");

    @Getter
    private String description;

    Frequency(String description){
        this.description = description;
    }
}
