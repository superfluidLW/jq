package com.jq.common.market.curve;

import com.jq.common.market.MktObj;

//to implement later
public class SwapCurve extends MktObj {

    private Double constR;

    public SwapCurve(double constR){
        this.constR = constR;
    }

    public SwapCurve bump(double shift){
        return new SwapCurve(constR+shift);
    }

    public double getDf(Double t){
        return Math.exp(-constR*t);
    }

    public double getZeroRate(Double t){
        return constR;
    }
}
