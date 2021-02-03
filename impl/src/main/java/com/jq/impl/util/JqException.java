package com.jq.impl.util;

import com.jq.common.output.JqMessageCode;
import lombok.Data;

@Data
public class JqException extends Exception {
    private JqMessageCode jqMessageCode;

    public JqException(JqMessageCode jqMessageCode, String errorInfo) {
        super(errorInfo);
        this.jqMessageCode = jqMessageCode;
    }
}
