package com.jq.common.convention;

import lombok.Getter;

public enum Venue {
    ShExg("上海证券交易所"),
    SzExg("深圳证券交易所"),
    Cib("中国银行间市场"),
    Otc("场外交易");

    @Getter
    private String description;

    Venue(String description){
        this.description = description;
    }
}
