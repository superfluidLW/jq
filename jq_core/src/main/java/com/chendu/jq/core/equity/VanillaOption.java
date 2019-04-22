package com.chendu.jq.core.equity;

import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqEnum.ValuationModel;
import com.chendu.jq.core.common.jqInterface.IDayCount;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.util.JsonUtils;
import lombok.Data;

import java.util.List;

@Data
public class VanillaOption extends Option {
    public Double strike;
    public IDayCount dayCount;

    public VanillaOption(){
        super();
    }

    @Override
    public Option bumpMaturity(int offset) {
        VanillaOption vo = JsonUtils.readValue(JsonUtils.writeValueAsString(this), VanillaOption.class);
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
    public List<JqCashflow> getCashflow(JqMarket jqMarket) {
        double price = jqMarket.getTickerMap().get(underlyingTicker).getPrice();
        return null;
    }

    @Override
    protected List<JqCashflow> calcPayoff() {
        return null;
    }
}
