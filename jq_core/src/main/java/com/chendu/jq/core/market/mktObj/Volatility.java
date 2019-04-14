package com.chendu.jq.core.market.mktObj;

import com.chendu.jq.core.common.jqInterface.IMktObj;
import lombok.Data;

@Data
public class Volatility implements IMktObj {
    private Double vol;

    @Override
    public Double bumpUp() {
        return vol + 0.01;
    }

    @Override
    public Double bumpDown() {
        return vol - 0.01;
    }
}
