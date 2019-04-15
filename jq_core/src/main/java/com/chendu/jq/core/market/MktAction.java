package com.chendu.jq.core.market;

import java.util.function.Function;

public class MktAction {
    public Function<JqMarket, Object> func;
    public MktObj mktObj;
    public Double bumpValue;

    public MktAction(Function<JqMarket, Object> func,
                     MktObj mktObj,
                     Double bumpValue){
        this.func = func;
        this.mktObj = mktObj;
        this.bumpValue = bumpValue;
    }
}
