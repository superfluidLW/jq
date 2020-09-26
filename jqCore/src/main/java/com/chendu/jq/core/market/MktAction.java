package com.chendu.jq.core.market;

import java.lang.reflect.Field;

public class MktAction {
    public Field jqMktField;
    public Object mktObj;
    public Double bumpValue;

    public MktAction(Field field,
                     Object mktObj,
                     Double bumpValue){
        this.jqMktField = field;
        this.mktObj = mktObj;
        this.bumpValue = bumpValue;
    }
}
