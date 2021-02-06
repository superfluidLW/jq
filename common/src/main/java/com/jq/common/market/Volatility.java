package com.jq.common.market;

import lombok.Data;

@Data
public class Volatility extends MktData {
    private Double vol;

    public Volatility(Double vol){
        this.vol = vol;
    }
}
