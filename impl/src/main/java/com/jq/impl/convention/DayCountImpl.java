package com.jq.impl.convention;

import com.jq.common.convention.DayCount;
import com.jq.impl.interfaces.IDayCount;

public class DayCountImpl {
    public static IDayCount get(DayCount dayCount){
        switch (dayCount){
            case Act360:
                return new Act360();
            case Act365:
                return new Act365();
            case ActAct:
                return new Act365();
            default:
                return new Act365();
        }

    }
}
