package com.chendu.jq.core.market.mktObj;

import com.chendu.jq.core.common.jqInterface.IMktObj;
import lombok.Data;

@Data
public class TickerInfo implements IMktObj {
    private Double price;

    @Override
    public Double bumpUp() {
        return price + 0.01;
    }

    @Override
    public Double bumpDown() {
        return price - 0.01;
    }
}
