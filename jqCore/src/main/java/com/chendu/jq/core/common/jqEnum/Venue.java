package com.chendu.jq.core.common.jqEnum;

import lombok.Getter;

public enum Venue {
    ShExg("上海证券交易所"),
    SzExg("深圳证券交易所"),
    Cib("中国银行间市场"),
    Otc("场外交易");

    @Getter
    private String nameCn;

    Venue(String nameCn){
        this.nameCn = nameCn;
    }
}
