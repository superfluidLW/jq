package com.chendu.jq.core.yieldCurve;

public class YieldCurve {

    public double getDf(Double r, Double t){
        return Math.exp(-r*t);
    }
}
