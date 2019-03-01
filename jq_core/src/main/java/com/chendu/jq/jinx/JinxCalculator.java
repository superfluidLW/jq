package com.chendu.jq.jinx;

import com.chendu.jq.excel.JqProp;
import com.chendu.jq.util.TableWithHeader;
import com.exceljava.jinx.ExcelFunction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JinxCalculator {
    @ExcelFunction(
            value = "valuate",
            isVolatile = true
    )
    public static String[][] valuate(String[][] trades) {
        log.warn("{}", trades);

        TableWithHeader tableWithHeader = new TableWithHeader(trades);
        return JqProp.GetAllProps();

    }
}
