package com.jq.common.market.curve;

import com.jq.common.convention.Currency;
import com.jq.common.market.MktData;
import lombok.Getter;

//to implement later
public class SwapCurve extends MktData {
    @Getter
    private Currency currency;
    @Getter
    private Double constR;

    public SwapCurve(Currency currency, double constR){
        this.currency = currency;
        this.constR = constR;
    }

    public SwapCurve bump(double shift){
        return new SwapCurve(currency, constR+shift);
    }

    public double getDf(Double t){
        return Math.exp(-constR*t);
    }

    public double getZeroRate(Double t){
        return constR;
    }
}
