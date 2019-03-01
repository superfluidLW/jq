package com.chendu.jq.util;

import com.chendu.jq.common.jqEnum.JqMessageCode;

public class JqException extends Exception {
    private JqMessageCode jqMessageCode;

    public JqException(JqMessageCode jqMessageCode, String errorInfo) {
        super(errorInfo);
        this.jqMessageCode = jqMessageCode;
    }
}
