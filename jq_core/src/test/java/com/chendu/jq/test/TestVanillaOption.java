package com.chendu.jq.test;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.dayCount.Act360;
import com.chendu.jq.core.common.dayCount.DayCount;
import com.chendu.jq.core.common.jqEnum.Currency;
import com.chendu.jq.core.common.jqEnum.DayCountType;
import com.chendu.jq.core.common.jqEnum.MktDataType;
import com.chendu.jq.core.equity.VanillaOption;
import com.chendu.jq.core.equity.calculator.analytical.EuropeanVanillaCalculator;
import com.chendu.jq.core.excel.XlFunc;
import com.chendu.jq.core.excel.XlUtil;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.mktObj.JqCurve;
import com.chendu.jq.core.market.mktObj.JqTicker;
import com.chendu.jq.core.market.mktObj.JqTickerInfo;
import com.chendu.jq.core.market.mktObj.JqVol;
import com.chendu.jq.core.util.JsonUtils;
import com.chendu.jq.core.util.TableWithHeader;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class TestVanillaOption {
    @Test
    public void printTemplate(){
        JqTicker jqTicker = new JqTicker("SH000300");
        System.out.println(jqTicker.toString());

        DayCount dc = new DayCount(DayCountType.Act360);
        System.out.println(dc.toString());
    }

    @Test
    public void validateCalculation(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额"};
        table[1] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "3000.0", "Act360", "SH000300", "2020-02-08", "Analytical", "看涨", "人民币", "1"};
        TableWithHeader twh = new TableWithHeader(table);

        List<JqTrade> vos = twh.toTrades();
        VanillaOption vanillaOption = (VanillaOption)vos.get(0);

        String[][] mktData = new String[4][2];
        mktData[0][0]= MktDataType.RiskfreeRate.name();
        mktData[0][1] = "0.05";

        mktData[1][0]= MktDataType.DividendRate.name();
        mktData[1][1] = "0.015";

        mktData[2][0]= MktDataType.S0.name();
        mktData[2][1] = "2800.0";

        mktData[3][0]= MktDataType.Vol.name();
        mktData[3][1] = "0.3";

        String[][] jqResult = XlFunc.jqCalc(XlFunc.transpose(table), mktData);
        System.out.println(JsonUtils.writeValueAsString(jqResult));
    }

    @Test
    public void TestValidateTradeLabel(){
        VanillaOption vo = new VanillaOption();
        assert vo.isValid();
    }

    @Test
    public void TestTradeConversion(){
        String[][] table = new String[3][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额"};
        table[1] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "100.605", "Act360", "SZ300000", "2020-02-08", "Analytical", "看涨", "人民币", "1"};
        table[2] = new String[]{"VanillaOption", "2019-02-28", "2021-02-28", "100.1", "Act365", "SH000001", "2020-02-08", "Analytical", "看涨", "人民币", "1"};
        TableWithHeader twh = new TableWithHeader(table);

        List<JqTrade> vos = twh.toTrades();
        assert vos.size() == 2;
    }
}
