package com.chendu.jq.core.market;

import java.lang.reflect.Field;

public class MktAction {
    public Field jqMktField;
    public MktObj jqMktObj;
    public Double bumpValue;

    public MktAction(Field field,
                     MktObj mktObj,
                     Double bumpValue){
        this.jqMktField = field;
        this.jqMktObj = mktObj;
        this.bumpValue = bumpValue;
    }
}
