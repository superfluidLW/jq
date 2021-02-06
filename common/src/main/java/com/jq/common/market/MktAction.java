package com.jq.common.market;

import java.lang.reflect.Field;

public class MktAction {
    public String field;
    public Object mktObj;
    public Double bumpValue;

    public MktAction(String field,
                     Object mktObj,
                     Double bumpValue){
        this.field = field;
        this.mktObj = mktObj;
        this.bumpValue = bumpValue;
    }
}
