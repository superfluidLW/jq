package com.chendu.jq.core.common.jqInterface;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.common.JqResult;
import com.chendu.jq.core.market.JqMarket;

public interface ICalculator {
    JqResult calculate(JqTrade trade, JqMarket jqMarket);
}
