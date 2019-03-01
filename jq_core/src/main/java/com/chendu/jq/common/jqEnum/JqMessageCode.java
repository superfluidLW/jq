package com.chendu.jq.common.jqEnum;

import lombok.Getter;

public enum JqMessageCode {
    Fatal("致命错误");

    @Getter
    private String nameCn;

    JqMessageCode(String nameCn){
        this.nameCn = nameCn;
    }
}
