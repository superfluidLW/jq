package com.chendu.jq.core.market.mktObj;

import com.chendu.jq.core.market.BumpableMktObj;
import lombok.Data;

@Data
public class JqTickerInfo extends BumpableMktObj {
    private Double price;

    public JqTickerInfo(Double price){
        this.price = price;
    }

    @Override
    public Double bumpUp() {
        return price + 0.01;
    }

    @Override
    public Double bumpDown() {
        return price - 0.01;
    }
}
