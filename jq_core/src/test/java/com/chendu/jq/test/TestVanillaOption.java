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
    public void validateVanillaCall(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额"};
        table[1] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "100.0", "Act365", "SH000300", "2020-02-28", "Analytical", "看涨", "人民币", "1"};

        String[][] mktData = new String[5][2];
        mktData[0][0]= MktDataType.RiskfreeRate.name();
        mktData[0][1] = "0.05";

        mktData[1][0]= MktDataType.DividendRate.name();
        mktData[1][1] = "0.02";

        mktData[2][0]= MktDataType.S0.name();
        mktData[2][1] = "102.0";

        mktData[3][0]= MktDataType.Vol.name();
        mktData[3][1] = "0.2";

        mktData[4][0]= MktDataType.MktDate.name();
        mktData[4][1] = "2019-02-28";

        JqResult jqResult = XlFunc.jqCalc(XlFunc.transpose(table), mktData);
        System.out.println(JsonUtils.writeValueAsString(jqResult));
        assert Math.abs(jqResult.getPv() - 10.438019) < 1.0e-6;
        assert Math.abs(jqResult.getDelta() - 0.623858) < 2.0e-6;
        assert Math.abs(jqResult.getGamma() - 0.018036) < 1.0e-6;
        assert Math.abs(jqResult.getRho() - 0.005320) < 1.0e-6;
        assert Math.abs(jqResult.getVega() - 0.375265) < 1.0e-6;
        assert Math.abs(jqResult.getTheta() - (-0.014091)) < 1.0e-6;
    }

    @Test
    public void validateVanillaPut(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额"};
        table[1] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "100.0", "Act365", "SH000300", "2020-02-28", "Analytical", "Put", "人民币", "1"};

        String[][] mktData = new String[5][2];
        mktData[0][0]= MktDataType.RiskfreeRate.name();
        mktData[0][1] = "0.05";

        mktData[1][0]= MktDataType.DividendRate.name();
        mktData[1][1] = "0.02";

        mktData[2][0]= MktDataType.S0.name();
        mktData[2][1] = "102.0";

        mktData[3][0]= MktDataType.Vol.name();
        mktData[3][1] = "0.2";

        mktData[4][0]= MktDataType.MktDate.name();
        mktData[4][1] = "2019-02-28";

        JqResult jqResult = XlFunc.jqCalc(XlFunc.transpose(table), mktData);
        System.out.println(JsonUtils.writeValueAsString(jqResult));
        assert Math.abs(jqResult.getPv() - 5.580697) < 1.0e-6;
        assert Math.abs(jqResult.getDelta() - (-0.356341)) < 2.0e-6;
        assert Math.abs(jqResult.getGamma() - 0.018036) < 1.0e-6;
        assert Math.abs(jqResult.getRho() - (-0.004193)) < 1.0e-6;
        assert Math.abs(jqResult.getVega() - 0.375265) < 1.0e-6;
        assert Math.abs(jqResult.getTheta() - (-0.006538)) < 1.0e-6;
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
