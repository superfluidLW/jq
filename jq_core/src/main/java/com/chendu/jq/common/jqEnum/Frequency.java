package com.chendu.jq.common.jqEnum;

import lombok.Getter;

public enum Frequency {
    Monthly("月"),
    Quarterly("季度"),
    Yearly("年");

    @Getter
    private String nameCn;

    Frequency(String nameCn){
        this.nameCn = nameCn;
    }

    public static Frequency fromNameCn(String nameCn) {
        for(Frequency fileFreqType : Frequency.values()) {
            if(fileFreqType.getNameCn().equals(nameCn) ) {
                return fileFreqType;
            }
        }
        return null;
    }
}
