package com.chendu.jq.test;

import com.chendu.jq.core.common.jqEnum.Venue;
import com.chendu.jq.core.util.JqParser;
import org.junit.Test;

public class TestJqEnum {
    @Test
    public void TestEnumParser(){
        Venue venue = JqParser.enumFromStr("ShExg", Venue.class);
        assert venue == Venue.ShExg;

        venue = JqParser.enumFromStr("上海证券交易所", Venue.class);
        assert venue == Venue.ShExg;
    }
}
