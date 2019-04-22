package com.chendu.jq.core.common.jqEnum;

import com.chendu.jq.core.common.dayCount.Act360;
import com.chendu.jq.core.common.dayCount.Act365;
import com.chendu.jq.core.common.dayCount.DayCount;
import com.chendu.jq.core.util.JqParser;
import lombok.Getter;

public enum DayCountType {
    Act360("Act360"),
    Act365("Act365"),
    ActAct("ActAct");

    @Getter
    private String nameCn;

    DayCountType(String nameCn){
        this.nameCn = nameCn;
    }

    public static DayCount impl(String dayCount){
        DayCountType dc = JqParser.enumFromStr(dayCount, DayCountType.class);
        switch (dc) {
            case Act360:
                return new Act360();
            case Act365:
                return new Act365();
            default:
                return new Act365();
        }
    }
}
