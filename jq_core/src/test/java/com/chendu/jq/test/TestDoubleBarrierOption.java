package com.chendu.jq.test;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.dayCount.DayCount;
import com.chendu.jq.core.common.jqEnum.DayCountType;
import com.chendu.jq.core.common.jqEnum.MktDataType;
import com.chendu.jq.core.equity.DigitalOption;
import com.chendu.jq.core.excel.XlFunc;
import com.chendu.jq.core.market.mktObj.JqTicker;
import com.chendu.jq.core.util.JsonUtils;
import com.chendu.jq.core.util.TableWithHeader;
import org.junit.Test;

import java.util.List;

public class TestDoubleBarrierOption {
    @Test
    public void printTemplate(){
        JqTicker jqTicker = new JqTicker("SH000300");
        System.out.println(jqTicker.toString());

        DayCount dc = new DayCount(DayCountType.Act365);
        System.out.println(dc.toString());
    }

    @Test
    public void validateDigitalCall(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额", "二值期权收益"};
        table[1] = new String[]{"DigitalOption", "2019-02-28", "2020-02-28", "100.0", "Act365", "SH000300", "2020-02-28", "Analytical", "看涨", "人民币", "1", "1.0"};

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
        assert Math.abs(jqResult.getPv() - 0.531954) < 1.0e-6;
        assert Math.abs(jqResult.getDelta() - 0.018397) < 2.0e-6;
        assert Math.abs(jqResult.getGamma() - (-0.000315)) < 1.0e-6;
        assert Math.abs(jqResult.getRho() - 0.00013439) < 1.0e-5;
        assert Math.abs(jqResult.getVega() - (-0.006560)) < 1.0e-6;
        assert Math.abs(jqResult.getTheta() - (0.0000981)) < 1.0e-6;
    }

    @Test
    public void validateDigitalPut(){
        String[][] table = new String[2][];

        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额", "二值期权收益"};
        table[1] = new String[]{"DigitalOption", "2019-02-28", "2020-02-28", "100.0", "Act365", "SH000300", "2020-02-28", "Analytical", "看跌", "人民币", "1", "1.0"};

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
        assert Math.abs(jqResult.getPv() - 0.419275) < 1.0e-6;
        assert Math.abs(jqResult.getDelta() - (-0.018397)) < 2.0e-6;
        assert Math.abs(jqResult.getGamma() - 0.000315) < 1.0e-6;
        assert Math.abs(jqResult.getRho() - (-0.00022952)) < 1.0e-6;
        assert Math.abs(jqResult.getVega() - 0.006560) < 1.0e-6;
        assert Math.abs(jqResult.getTheta() - (0.000032)) < 1.0e-6;
    }


    @Test
    public void validateDigitalCallMc(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "蒙特卡洛样本量", "计算MC希腊值", "期权方向", "本币币种", "名义面额", "二值期权收益"};
        table[1] = new String[]{"DigitalOption", "2019-02-28", "2020-02-28", "100.0", "Act365", "SH000300", "2020-02-28", "MonteCarlo", "25000", "false", "看涨", "人民币", "1", "1.0"};

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
        assert Math.abs(jqResult.getPv() - 0.531954) < 0.01;
    }

    @Test
    public void TestValidateTradeLabel(){
        DigitalOption vo = new DigitalOption();
        assert vo.isValid();
    }

    @Test
    public void TestTradeConversion(){
        String[][] table = new String[3][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额"};
        table[1] = new String[]{"DigitalOption", "2019-02-28", "2020-02-28", "100.605", "Act360", "SZ300000", "2020-02-08", "Analytical", "看涨", "人民币", "1"};
        table[2] = new String[]{"DigitalOption", "2019-02-28", "2021-02-28", "100.1", "Act365", "SH000001", "2020-02-08", "Analytical", "看涨", "人民币", "1"};
        TableWithHeader twh = new TableWithHeader(table);

        List<JqTrade> vos = twh.toTrades();
        assert vos.size() == 2;
    }
}
