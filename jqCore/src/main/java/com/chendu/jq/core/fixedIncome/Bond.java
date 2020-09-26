package com.chendu.jq.core.fixedIncome;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.market.JqMarket;

import java.util.List;

public class Bond extends JqTrade {
    @Override
    public JqResult calc(JqMarket jqMarket) {
        return null;
    }

    @Override
    public List<JqCashflow> cashflows(JqMarket jqMarket) {
        return null;
    }
}
