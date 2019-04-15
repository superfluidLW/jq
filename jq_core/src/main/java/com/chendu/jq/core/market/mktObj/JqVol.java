package com.chendu.jq.core.market.mktObj;

import com.chendu.jq.core.market.BumpableMktObj;
import lombok.Data;

@Data
public class JqVol extends BumpableMktObj {
    private Double vol;

    public JqVol(Double vol){
        this.vol = vol;
    }

    @Override
    public Double bumpUp() {
        return vol + 0.01;
    }

    @Override
    public Double bumpDown() {
        return vol - 0.01;
    }
}
