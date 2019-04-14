package com.chendu.jq.core.market.mktObj;

//to implement later
public class YieldCurve {

    private Double constR;

    public YieldCurve(double constR){
        this.constR = constR;
    }

    public double getDf(Double t){
        return Math.exp(-constR*t);
    }
}
