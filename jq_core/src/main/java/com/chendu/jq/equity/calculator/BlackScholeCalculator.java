package com.chendu.jq.equity.calculator;

import com.chendu.jq.JqTrade;
import com.chendu.jq.common.JqResult;
import com.chendu.jq.common.jqInterface.ICalculator;
import com.chendu.jq.equity.Option;
import com.chendu.jq.market.JqMarket;

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
