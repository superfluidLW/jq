package com.jq.impl.calculator.equity;

import com.jq.common.output.Result;
import com.jq.common.deal.Deal;
import com.jq.common.convention.ValuationModel;
import com.jq.impl.interfaces.ICalculator;
import com.jq.common.deal.equity.Option;
import com.jq.common.market.Market;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;

@Slf4j
public abstract class OptionCalculator implements ICalculator {
    public static NormalDistribution normal = new NormalDistribution(0.0, 1.0);

    public Result calc(Deal trade, Market market) {
        if (!(trade instanceof Option)) {
            log.info("Not an option trade", this.getClass().getName(), trade.getClass().getName());
        }
        Result result = new Result();

        Option option = (Option)trade;
        Double pv = calcPv(option, market);

        if(option.getValuationModel() == ValuationModel.MonteCarlo && !option.getCalcMcGreeks()){
            result.setPv(pv);
            return result;
        }

        Double deltaSpot = market.tickerPrice(option.getUnderlying()) / 10000.0;
        market.updateActions(market.bumpSecuritySpot(option.getUnderlying(), deltaSpot));
        Double pvSpotUp = calcPv(option, market);
        market.updateActions(market.bumpSecuritySpot(option.getUnderlying(), -deltaSpot));
        Double pvSpotDown = calcPv(option, market);

        Double delta = (pvSpotUp-pvSpotDown)/(2*deltaSpot);
        Double gamma = (pvSpotUp + pvSpotDown - 2.0*pv)/(deltaSpot*deltaSpot);

        market.updateActions(market.bumpVol(option.getUnderlying(), 0.01));
        Double pvVolUp = calcPv(trade, market);
        market.updateActions(market.bumpVol(option.getUnderlying(),-0.01));
        Double pvVolDown = calcPv(trade, market);
        Double vega = (pvVolUp - pvVolDown) / 2.0;

        market.updateActions(market.bumpYieldCurve(option.getDomCurrency(),0.0001));
        Double pvRup = calcPv(trade, market);
        market.updateActions(market.bumpYieldCurve(option.getDomCurrency(),-0.0001));
        Double pvRdown = calcPv(trade, market);
        Double rho = (pvRup - pvRdown)/2.0;

        market.updateActions(new ArrayList<>());
        Option tradeMaturityBumped = ((Option) trade).bumpMaturity(-1);
        Double pvMaturityBumped = calcPv(tradeMaturityBumped, market);
        Double theta = pvMaturityBumped - pv;

        result.setPv(pv);
        result.setDelta(delta);
        result.setVega(vega);
        result.setRho(rho);
        result.setGamma(gamma);
        result.setTheta(theta);
        return result;
    }

    public abstract Double calcPv(Deal trade, Market market);
}
