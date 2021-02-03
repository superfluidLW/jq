package com.jq.common.convention;

import lombok.Getter;

public enum DayCount {
    Act360("Act360"),
    Act365("Act365"),
    ActAct("ActAct");

    @Getter
    private String description;

    DayCount(String description){
        this.description = description;
    }
}
