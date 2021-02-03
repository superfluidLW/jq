package com.jq.common.convention;

import lombok.Getter;

public enum Currency {
    Cny("人民币");

    @Getter
    private String description;

    Currency(String description){
        this.description = description;
    }
}
