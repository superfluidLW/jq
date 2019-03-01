package com.chendu.jq.fixedIncome;

import com.chendu.jq.JqTrade;
import com.chendu.jq.common.JqCashflow;
import com.chendu.jq.common.JqResult;
import com.chendu.jq.market.JqMarket;

import java.util.List;

public class Bond extends JqTrade {
    @Override
    public JqResult calc(JqMarket jqMarket) {
        return null;
    }

    @Override
    public List<JqCashflow> getCashflow(JqMarket jqMarket) {
        return null;
    }
}
