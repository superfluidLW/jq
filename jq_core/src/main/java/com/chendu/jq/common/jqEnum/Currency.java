package com.chendu.jq.common.jqEnum;

import lombok.Getter;

public enum Currency {
    Cny("人民币");

    @Getter
    private String nameCn;

    Currency(String nameCn){
        this.nameCn = nameCn;
    }
}
