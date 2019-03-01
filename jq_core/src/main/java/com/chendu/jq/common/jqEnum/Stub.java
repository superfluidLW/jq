package com.chendu.jq.common.jqEnum;

import lombok.Getter;

public enum Stub {
    ShortStart("短头"),
    ShortEnd("短尾"),
    LongStart("长头"),
    LongEnd("长尾");

    @Getter
    private String nameCn;

    Stub(String nameCn){
        this.nameCn = nameCn;
    }
}
