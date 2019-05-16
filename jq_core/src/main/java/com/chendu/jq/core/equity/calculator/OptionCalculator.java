package com.chendu.jq.core.equity.calculator;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqEnum.ValuationModel;
import com.chendu.jq.core.common.jqInterface.ICalculator;
import com.chendu.jq.core.equity.Option;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.util.JqLog;

import java.util.ArrayList;

public abstract class OptionCalculator implements ICalculator {
    public JqResult calc(JqTrade trade, JqMarket jqMarket) {
        if (!(trade instanceof Option)) {
            JqLog.info("Not an option trade", this.getClass().getName(), trade.getClass().getName());
        }
        JqResult jqResult = new JqResult();

        Option option = (Option)trade;
        Double pv = calcPv(option, jqMarket);

        if(option.valuationModel == ValuationModel.MonteCarlo && !option.getCalcMcGreeks()){
            jqResult.setPv(pv);
            return jqResult;
        }

        Double deltaSpot = 0.01;
        jqMarket.updateActions(option.bumpSpot(deltaSpot));
        Double pvSpotUp = calcPv(option, jqMarket);
        jqMarket.updateActions(option.bumpSpot(-deltaSpot));
        Double pvSpotDown = calcPv(option, jqMarket);

        Double delta = (pvSpotUp-pvSpotDown)/(2*deltaSpot);
        Double gamma = (pvSpotUp + pvSpotDown - 2.0*pv)/(deltaSpot*deltaSpot);

        jqMarket.updateActions(option.bumpVol(0.01));
        Double pvVolUp = calcPv(trade, jqMarket);
        jqMarket.updateActions(option.bumpVol(-0.01));
        Double pvVolDown = calcPv(trade, jqMarket);
        Double vega = (pvVolUp - pvVolDown) / 2.0;

        jqMarket.updateActions(option.bumpYieldCurve(0.0001));
        Double pvRup = calcPv(trade, jqMarket);
        jqMarket.updateActions(option.bumpYieldCurve(-0.0001));
        Double pvRdown = calcPv(trade, jqMarket);
        Double rho = (pvRup - pvRdown)/2.0;

        jqMarket.updateActions(new ArrayList<>());
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
