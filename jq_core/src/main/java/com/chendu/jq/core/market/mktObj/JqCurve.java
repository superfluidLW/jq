package com.chendu.jq.core.market.mktObj;

import com.chendu.jq.core.market.BumpableMktObj;

//to implement later
public class JqCurve extends BumpableMktObj {

    private Double constR;

    public JqCurve(double constR){
        this.constR = constR;
    }

    public double getDf(Double t){
        return Math.exp(-constR*t);
    }

    @Override
    protected Double bumpUp() {
        return null;
    }

    @Override
    protected Double bumpDown() {
        return null;
    }
}
