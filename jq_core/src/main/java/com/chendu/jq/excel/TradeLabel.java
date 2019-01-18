package com.chendu.jq.excel;

import lombok.Getter;

public enum TradeLabel {
    StartDate("开始日期"),
    MaturityDate("到期日期"),
    StrikeDate("行权日期");

    @Getter
    private String nameCn;

    TradeLabel(String nameCn){
        this.nameCn = nameCn;
    }

    public static TradeLabel fromNameCn(String nameCn) {
        for(TradeLabel label : TradeLabel.values()) {
            if(label.getNameCn().equals(nameCn) ) {
                return label;
            }
        }
        return null;
    }
}
