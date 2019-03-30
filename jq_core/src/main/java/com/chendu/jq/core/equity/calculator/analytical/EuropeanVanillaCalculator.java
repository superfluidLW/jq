package com.chendu.jq.core.equity.calculator.analytical;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqInterface.ICalculator;
import com.chendu.jq.core.equity.VanillaOption;
import com.chendu.jq.core.market.JqMarket;

public class EuropeanVanillaCalculator implements ICalculator {

    @Override
    public JqResult calculate(JqTrade trade, JqMarket jqMarket) {
        if(! (trade instanceof VanillaOption)){
            System.out.println(String.format("%s is only used to valuate European vanilla Option, cannot valuate %s", this.getClass().getName(), trade.getClass().getName()));
        }

        JqResult jqResult = new JqResult();


        return null;
    }
}
