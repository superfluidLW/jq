package com.jq.common.convention;

import lombok.Getter;

public enum OptionDirection {
    Call("看涨"),
    Put("看跌");

    @Getter
    private String description;

    OptionDirection(String description){
        this.description = description;
    }
}
