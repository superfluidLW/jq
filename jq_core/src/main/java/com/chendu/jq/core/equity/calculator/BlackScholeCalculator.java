package com.chendu.jq.core.equity.calculator;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqInterface.ICalculator;
import com.chendu.jq.core.equity.Option;
import com.chendu.jq.core.market.JqMarket;

public class BlackScholeCalculator implements ICalculator {

    @Override
    public JqResult calculate(JqTrade trade, JqMarket jqMarket) {
        if(! (trade instanceof Option)){
            System.out.println(String.format("%s is only used to valuate option, cannot valuate %s", this.getClass().getName(), trade.getClass().getName()));
        }

        JqResult jqResult = new JqResult();


        return null;
    }
}
