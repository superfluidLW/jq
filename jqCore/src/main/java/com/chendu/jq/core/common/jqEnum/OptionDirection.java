package com.chendu.jq.core.common.jqEnum;

import lombok.Getter;

public enum OptionDirection {
    Call("看涨"),
    Put("看跌");

    @Getter
    private String nameCn;

    OptionDirection(String nameCn){
        this.nameCn = nameCn;
    }
}
