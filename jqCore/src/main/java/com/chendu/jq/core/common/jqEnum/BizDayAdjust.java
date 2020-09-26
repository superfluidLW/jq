package com.chendu.jq.core.common.jqEnum;

import lombok.Getter;

public enum BizDayAdjust {
    None("无"),
    Following("下一工作日"),
    ModifiedFollowing("调整的下一工作日"),
    Previous("前一工作日"),
    ModifiedPrevious("调整的前一工作日");

    @Getter
    private String nameCn;

    BizDayAdjust(String nameCn){
        this.nameCn = nameCn;
    }
}
