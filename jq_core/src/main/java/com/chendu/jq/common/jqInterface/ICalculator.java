package com.chendu.jq.common.jqInterface;

import com.chendu.jq.JqTrade;
import com.chendu.jq.common.JqResult;
import com.chendu.jq.market.JqMarket;

public interface ICalculator {
    JqResult calculate(JqTrade trade, JqMarket jqMarket);
}
