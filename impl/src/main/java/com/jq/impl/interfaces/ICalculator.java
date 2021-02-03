package com.jq.impl.interfaces;

import com.jq.common.output.ReqResult;
import com.jq.common.output.Result;
import com.jq.common.deal.Deal;
import com.jq.common.market.Market;

import java.util.List;

public interface ICalculator {
    Result calc(Deal deal, Market market);

//    Result calc(Deal deal, Market market, List<ReqResult> reqResults);
}
