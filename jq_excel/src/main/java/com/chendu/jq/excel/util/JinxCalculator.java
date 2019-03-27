package com.chendu.jq.excel.util;

import com.chendu.jq.core.excel.JqProp;
import com.chendu.jq.core.util.TableWithHeader;
import com.exceljava.jinx.ExcelFunction;
import lombok.extern.slf4j.Slf4j;

public class JinxCalculator {
    @ExcelFunction(
            value = "valuate",
            isVolatile = true
    )
    public static String[][] valuate(String[][] trades) {
        TableWithHeader tableWithHeader = new TableWithHeader(trades);
        return JqProp.GetAllProps();

    }
}
