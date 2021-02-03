package com.jq.common.convention;

import lombok.Getter;

public enum Stub {
    ShortStart("短头"),
    ShortEnd("短尾"),
    LongStart("长头"),
    LongEnd("长尾");

    @Getter
    private String description;

    Stub(String description){
        this.description = description;
    }
}
