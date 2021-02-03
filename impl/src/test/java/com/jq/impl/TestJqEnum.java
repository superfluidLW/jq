package com.jq.impl;

import com.jq.common.convention.Venue;
import com.jq.impl.util.JqParser;
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
