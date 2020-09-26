package com.chendu.jq.core.market.mktObj;

import com.chendu.jq.core.market.MktObj;
import lombok.Data;

@Data
public class JqVol extends MktObj {
    private Double vol;

    public JqVol(Double vol){
        this.vol = vol;
    }
}
