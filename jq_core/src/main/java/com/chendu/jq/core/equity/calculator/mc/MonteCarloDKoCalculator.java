package com.chendu.jq.core.equity.calculator.mc;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.jqEnum.OptionDirection;
import com.chendu.jq.core.equity.DoubleBarrierOption;
import com.chendu.jq.core.equity.calculator.OptionCalculator;
import com.chendu.jq.core.market.JqMarket;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.time.LocalDate;
import java.util.Random;

public class MonteCarloDKoCalculator extends OptionCalculator {
  public static NormalDistribution normal = new NormalDistribution(0.0, 1.0);

  public Integer steps;
  public Integer nPaths;

  public MonteCarloDKoCalculator(Integer steps, Integer nPaths){
    this.steps = steps;
    this.nPaths = nPaths;
  }

  @Override
  public Double calcPv(JqTrade trade, JqMarket jqMarket) {
    DoubleBarrierOption option = (DoubleBarrierOption)trade;
    Double strike = option.getStrike();
    Double spot = jqMarket.tickerPrice(option.getUnderlyingTicker());
    LocalDate exerciseDate = ((DoubleBarrierOption) trade).exerciseDate;
    Double volatility = jqMarket.tickerVol(option.getUnderlyingTicker());

    Double yearFrac = option.getDayCount().yearFraction(jqMarket.getMktDate(), exerciseDate);
    Double riskFreeRate = jqMarket.jqCurve(option.getDomCurrency()).getZeroRate(yearFrac);
    Double discFact = jqMarket.jqCurve(option.getDomCurrency()).getDf(yearFrac);

    Double dt = yearFrac/steps;
    Double upBarrier = option.getUBarrier();
    Double lowBarrier = option.getLBarrier();
    Random random = new Random();

    Double pvAvg = 0.0;
    Double outProb = 0.0;
    for (int i = 0; i < nPaths; ++i)
    {
      Double stockPrice = spot;
      boolean isOut = false;
      Double pv = 0.0;

      int j = 0;
      while (!isOut && j < steps - 1)
      {
        // generate N(0,1) sample
        Double drift = riskFreeRate - 0.5*volatility*volatility;
        Double newStockPrice = stockPrice*Math.exp(volatility*Math.sqrt(dt)*normal.sample() + drift*dt);
        if (newStockPrice > option.getUBarrier() || newStockPrice < option.getLBarrier()) // knock-out happened
        {
          isOut = true;
          outProb += 1.0/nPaths;
        }
        else // possible barrier crossing giving two end points
        {
          Double pUp = Math.exp(-2*Math.log(upBarrier/stockPrice)*Math.log(upBarrier/newStockPrice)/(dt*volatility*volatility));
          Double pLow =
              Math.exp(-2*Math.log(lowBarrier/stockPrice)*Math.log(lowBarrier/newStockPrice)/(dt*volatility*volatility));
          Double pOut = pUp + pLow;
          if (random.nextDouble() < pOut)
          {
            isOut = true;
            outProb += 1.0/nPaths;
          }
        }
        stockPrice = newStockPrice;
        ++j;
      }

      // payoff at exercise if not knocked out
      if (!isOut)
      {
        Double drift = riskFreeRate - 0.5*volatility*volatility;
        stockPrice *= Math.exp(volatility*Math.sqrt(dt)*normal.sample() + drift*dt);
        pv = option.optionDirection == OptionDirection.Call
            ? Math.max(stockPrice - strike, 0)
            : Math.max(strike - stockPrice, 0);
        pvAvg += (pv + option.getKiCoupon());
      }
      else
      {
        pvAvg += option.getKoRebate();
      }
    }
    pvAvg /= nPaths;
    return pvAvg *  discFact * option.getNotional();
  }
}
