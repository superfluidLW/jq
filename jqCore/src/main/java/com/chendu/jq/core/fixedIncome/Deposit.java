package com.chendu.jq.core.fixedIncome;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.market.JqMarket;

import java.util.List;

public class Deposit extends JqTrade {
    public Double depoRate;

    @Override
    public JqResult calc(JqMarket jqMarket) {
        Double maturityTime = dayCount.yearFraction(jqMarket.getMktDate(), maturityDate);
        Double dfAtMaturity = jqMarket.jqCurve(domCurrency).getDf(maturityTime);

        JqResult jqResult = new JqResult();
        jqResult.setPv(notional*(1+depoRate*maturityTime)*dfAtMaturity);
        return jqResult;
    }

    @Override
    public List<JqCashflow> cashflows(JqMarket jqMarket) {
        return null;
    }
}
