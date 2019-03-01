package com.chendu.jq.common.jqEnum;

import com.chendu.jq.common.dayCount.Act360;
import com.chendu.jq.common.dayCount.Act365;
import com.chendu.jq.common.jqInterface.IDayCount;
import com.chendu.jq.util.JqParser;
import lombok.Getter;

public enum DayCount {
    Act360("Act360"),
    Act365("Act365"),
    ActAct("ActAct");

    @Getter
    private String nameCn;

    DayCount(String nameCn){
        this.nameCn = nameCn;
    }

    public static IDayCount impl(String dayCount){
        DayCount dc = JqParser.enumFromStr(dayCount, DayCount.class);
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
