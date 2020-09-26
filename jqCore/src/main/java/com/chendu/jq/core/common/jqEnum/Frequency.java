package com.chendu.jq.core.common.jqEnum;

import lombok.Getter;

public enum Frequency {
    Continuous("连续"),
    Monthly("每月"),
    Quarterly("每季度"),
    SemiAnnual("每半年"),
    Annually("每年");

    @Getter
    private String nameCn;

    Frequency(String nameCn){
        this.nameCn = nameCn;
    }
}
