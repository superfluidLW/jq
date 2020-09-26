package com.chendu.jq.core.util;

import com.chendu.jq.core.common.jqEnum.JqMessageCode;
import lombok.Data;

@Data
public class JqException extends Exception {
    private JqMessageCode jqMessageCode;

    public JqException(JqMessageCode jqMessageCode, String errorInfo) {
        super(errorInfo);
        this.jqMessageCode = jqMessageCode;
    }
}
