package com.chendu.jq.test;

import com.chendu.jq.core.util.JqParser;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class TestJqDate {
    @Test
    public void testParseDateStr(){
        LocalDate date = JqParser.jqDateFromStr("20190202");

        LocalDate benchmarkDate = LocalDate.of(2019, 02, 02);
        assert date.equals(benchmarkDate);
    }
}
