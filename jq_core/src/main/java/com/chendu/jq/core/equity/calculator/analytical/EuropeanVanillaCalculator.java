package com.chendu.jq.core.equity.calculator.analytical;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqInterface.ICalculator;
import com.chendu.jq.core.equity.VanillaOption;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.util.JqLog;

import java.time.LocalDate;

public class EuropeanVanillaCalculator implements ICalculator {

    @Override
    public JqResult calculate(JqTrade trade, JqMarket jqMarket) {
        if(! (trade instanceof VanillaOption)){
            JqLog.info("Used to valuate European vanilla Option, cannot valuate", this.getClass().getName(), trade.getClass().getName());
        }

        VanillaOption vanillaOption = (VanillaOption)trade;
        JqResult jqResult = new JqResult();

        LocalDate exerciseDate = vanillaOption.getExerciseDates().get(0);
        LocalDate maturityDate = vanillaOption.getMaturityDate();
        Double exerciseTime = vanillaOption.getDayCount().yearFraction(jqMarket.getMktDate(), exerciseDate);
        Double maturityTime = vanillaOption.getDayCount().yearFraction(jqMarket.getMktDate(), maturityDate);

        Double rfAtExercise = jqMarket.getYieldCurveMap().get(vanillaOption.getDomCurrency()).getDf(0.05, exerciseTime);
        Double vol = jqMarket.getVolatilityMap().get(vanillaOption.getUnderlyingAssetSymbol());

        vanillaOption.getOptionDirection();
        return jqResult;
    }
}
