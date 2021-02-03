package com.jq.common.output;

import lombok.Getter;

public enum JqMessageCode {
    Succeed("成功"),
    TimeOut("超时"),
    Fatal("致命错误");

    @Getter
    private String nameCn;

    JqMessageCode(String nameCn){
        this.nameCn = nameCn;
    }
}
