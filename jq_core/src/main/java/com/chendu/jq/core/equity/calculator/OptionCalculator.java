package com.chendu.jq.core.equity.calculator;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqInterface.ICalculator;
import com.chendu.jq.core.equity.Option;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.util.JqLog;

public abstract class OptionCalculator implements ICalculator {
    public JqResult calc(JqTrade trade, JqMarket jqMarket) {
        if (!(trade instanceof Option)) {
            JqLog.info("Not an option trade", this.getClass().getName(), trade.getClass().getName());
        }
        JqResult jqResult = new JqResult();

        Option option = (Option)trade;
        Double pv = calcPv(option, jqMarket);

        Double deltaSpot = 0.1;
        jqMarket.updateActions(option.bumpSpot(deltaSpot));
        Double pvSpotUp = calcPv(option, jqMarket);
        jqMarket.updateActions(option.bumpSpot(-deltaSpot));
        Double pvSpotDown = calcPv(option, jqMarket);
        Double delta = (pvSpotUp-pvSpotDown)/(2*deltaSpot);
        
        jqResult.setPv(pv);
        jqResult.setDelta(delta);
        return jqResult;
    }

    public abstract Double calcPv(JqTrade trade, JqMarket jqMarket);
}
