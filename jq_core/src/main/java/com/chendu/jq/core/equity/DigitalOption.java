package com.chendu.jq.core.equity;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.dayCount.Act360;
import com.chendu.jq.core.common.jqEnum.Currency;
import com.chendu.jq.core.common.jqEnum.OptionDirection;
import com.chendu.jq.core.common.jqEnum.TradeType;
import com.chendu.jq.core.common.jqEnum.ValuationModel;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.mktObj.JqTicker;
import com.chendu.jq.core.util.JsonUtils;
import lombok.Data;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Data
public class DigitalOption extends Option {


    public DigitalOption(){
        super();
        tradeType = TradeType.DigitalOption;
    }

    @Override
    public Option bumpMaturity(int offset) {
        DigitalOption vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), DigitalOption.class);
        vo.setMaturityDate(this.maturityDate.plusDays(offset));
        return vo;
    }

    @Override
    public JqResult calc(JqMarket jqMarket) {
        JqResult jqResult = new JqResult();

        if(valuationModel == ValuationModel.Analytical){

        }
        else if(valuationModel == ValuationModel.MonteCarlo){

        }

        return jqResult;
    }

    @Override
    public List<JqCashflow> cashflows(JqMarket jqMarket) {
        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
        return null;
    }

    public static String[][] templateTradeData() {
        DigitalOption vanillaOption = new DigitalOption();
        vanillaOption.setStartDate(LocalDate.now());
        vanillaOption.setMaturityDate(LocalDate.now().plusYears(1));
        vanillaOption.setExerciseDates(Arrays.asList(LocalDate.now().plusYears(1)));
        vanillaOption.setStrike(1000.0);
        vanillaOption.setOptionDirection(OptionDirection.Call);
        vanillaOption.setUnderlyingTicker(new JqTicker("SH000300"));
        vanillaOption.setDayCount(new Act360());
        vanillaOption.setNotional(1.0);
        vanillaOption.setDomCurrency(Currency.Cny);
        vanillaOption.setValuationModel(ValuationModel.Analytical);
        return JqTrade.templateTradeData(DigitalOption.class, vanillaOption);
    }

    @Override
    protected List<JqCashflow> calcPayoff() {
        return null;
    }
}
