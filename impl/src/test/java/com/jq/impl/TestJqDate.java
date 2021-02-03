package com.jq.impl;

import com.jq.impl.util.JqParser;
import org.junit.Test;

import java.time.LocalDate;

public class TestJqDate {
    @Test
    public void testParseDateStr(){
        LocalDate date = JqParser.jqDateFromStr("20190202");

        LocalDate benchmarkDate = LocalDate.of(2019, 02, 02);
        assert date.equals(benchmarkDate);
    }
}
