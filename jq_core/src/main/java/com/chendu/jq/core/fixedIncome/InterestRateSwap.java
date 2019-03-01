package com.chendu.jq.core.fixedIncome;

import com.chendu.jq.common.JqResult;
import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.market.JqMarket;
import lombok.Data;

import java.util.List;

@Data
public class InterestRateSwap extends JqTrade {

    @Override
    public JqResult calc(JqMarket jqMarket) {
        return null;
    }

    @Override
    public List<JqCashflow> getCashflow(JqMarket jqMarket) {
        return null;
    }
}
