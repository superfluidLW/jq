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
        jqMarket.updateActions(option.bumpSpot(-deltaSpot));
        Double pvSpotUp = calcPv(option, jqMarket);
        Double pvSpotDown = calcPv(option, jqMarket);

        Double delta = (pvSpotUp-pvSpotDown)/(2*deltaSpot);
        Double gamma = (pvSpotUp + pvSpotDown - 2.0*pv)/(deltaSpot*deltaSpot);

        jqMarket.updateActions(option.bumpVol(0.01));
        Double pvVolUp = calcPv(trade, jqMarket);
        Double vega = (pvVolUp - pv);

        jqMarket.updateActions(option.bumpYieldCurve(0.0001));
        Double pvRup = calcPv(trade, jqMarket);
        Double rho = (pvRup - pv);

        Option tradeMaturityBumped = ((Option) trade).bumpMaturity(-1);
        Double pvMaturityBumped = calcPv(tradeMaturityBumped, jqMarket);
        Double theta = pvMaturityBumped - pv;

        jqResult.setPv(pv);
        jqResult.setDelta(delta);
        jqResult.setGamma(gamma);
        jqResult.setVega(vega);
        jqResult.setRho(rho);
        jqResult.setTheta(theta);
        return jqResult;
    }

    public abstract Double calcPv(JqTrade trade, JqMarket jqMarket);
}
