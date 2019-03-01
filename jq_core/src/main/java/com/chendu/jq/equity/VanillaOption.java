package com.chendu.jq.equity;

import com.chendu.jq.common.JqCashflow;
import com.chendu.jq.common.JqResult;
import com.chendu.jq.common.jqEnum.ValuationModel;
import com.chendu.jq.common.jqInterface.IDayCount;
import com.chendu.jq.market.JqMarket;
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
        double price = jqMarket.getQuote().get(underlyingAssetSymbol);
        return null;
    }

    @Override
    protected List<JqCashflow> calcPayoff() {
        return null;
    }
}
