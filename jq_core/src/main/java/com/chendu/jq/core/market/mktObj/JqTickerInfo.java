package com.chendu.jq.core.market.mktObj;

import com.chendu.jq.core.market.MktObj;
import lombok.Data;

@Data
public class JqTickerInfo extends MktObj {
    private Double price;

    public JqTickerInfo(Double price){
        this.price = price;
    }
}
