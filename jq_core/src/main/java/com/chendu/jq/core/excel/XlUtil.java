package com.chendu.jq.core.excel;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.jqEnum.Currency;
import com.chendu.jq.core.common.jqEnum.MktDataType;
import com.chendu.jq.core.equity.Option;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.mktObj.JqCurve;
import com.chendu.jq.core.market.mktObj.JqTicker;
import com.chendu.jq.core.market.mktObj.JqTickerInfo;
import com.chendu.jq.core.market.mktObj.JqVol;
import com.chendu.jq.core.util.JqParser;

import java.time.LocalDate;

public class XlUtil {
    public static JqMarket toJqMarket(JqTrade trade, String[][] mktData){
        JqMarket jqMarket = new JqMarket();

        for(int i = 0; i < mktData.length; ++i){
                MktDataType mdt = JqParser.enumFromStr(mktData[i][0], MktDataType.class);

                if(mdt == MktDataType.MktDate){
                    jqMarket.setMktDate(JqParser.jqDateFromStr(mktData[i][1]));
                }
                else {
                    Double rate = Double.parseDouble(mktData[i][1]);
                    if (mdt == MktDataType.RiskfreeRate) {
                        jqMarket.getYieldCurveMap().put(Currency.Cny, new JqCurve(rate));
                    }
                    if (mdt == MktDataType.DividendRate) {
                        jqMarket.getDividendCurveMap().put(((Option) trade).getUnderlyingTicker(), new JqCurve(rate));
                    }
                    if (mdt == MktDataType.S0) {
                        jqMarket.getTickerMap().put(((Option) trade).getUnderlyingTicker(), new JqTickerInfo(rate));
                    }
                    if (mdt == MktDataType.Vol) {
                        jqMarket.getVolatilityMap().put(((Option) trade).getUnderlyingTicker(), new JqVol(rate));
                    }
                }
        }

        return jqMarket;
    }
}
