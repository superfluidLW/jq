package com.chendu.jq.core.yieldCurve;

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
