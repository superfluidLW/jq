package com.chendu.jq.core.market.mktObj;

import com.chendu.jq.core.market.MktObj;

//to implement later
public class JqCurve extends MktObj {

    private Double constR;

    public JqCurve(double constR){
        this.constR = constR;
    }

    public double getDf(Double t){
        return Math.exp(-constR*t);
    }
}
