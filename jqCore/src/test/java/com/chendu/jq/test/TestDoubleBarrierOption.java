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
    public void validateDoubleBarrierCall(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额", "低障碍", "高障碍", "敲出补偿", "敲入基础票息", "参与率"};
        table[1] = new String[]{"DoubleBarrierOption", "2020-03-01", "2021-03-01", "1.05", "Act365", "SH000300", "2021-03-01", "Analytical", "看涨", "人民币", "1.0", "0.75", "1.25", "0.00", "0.00", "1.00"};

        String[][] mktData = new String[5][2];
        mktData[0][0]= MktDataType.RiskfreeRate.name();
        mktData[0][1] = "0.03";

        mktData[1][0]= MktDataType.DividendRate.name();
        mktData[1][1] = "0.00";

        mktData[2][0]= MktDataType.S0.name();
        mktData[2][1] = "1.0";

        mktData[3][0]= MktDataType.Vol.name();
        mktData[3][1] = "0.2";

        mktData[4][0]= MktDataType.MktDate.name();
        mktData[4][1] = "2020-03-01";

        JqResult jqResult = XlFunc.jqCalc(XlFunc.transpose(table), mktData);

        System.out.println(JsonUtils.writeValueAsString(jqResult));
        assert Math.abs(jqResult.getPv() - 0.010845) < 1.0e-6;
        assert Math.abs(jqResult.getDelta() - 0.005738) < 1e-6;
        assert Math.abs(jqResult.getGamma() - (-0.487205)) < 1.0e-6;
        assert Math.abs(jqResult.getRho() - 0.00000241) < 1.0e-5;
        assert Math.abs(jqResult.getVega() - (-0.001063)) < 1.0e-6;
        assert Math.abs(jqResult.getTheta() - (0.000027)) < 1.0e-6;
    }

    @Test
    public void validateDoubleBarrierPut(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额", "低障碍", "高障碍", "敲出补偿", "敲入基础票息", "参与率"};
        table[1] = new String[]{"DoubleBarrierOption", "2020-03-01", "2021-03-01", "1.05", "Act365", "SH000300", "2021-03-01", "Analytical", "看跌", "人民币", "1.0", "0.75", "1.25", "0.00", "0.00", "1.00"};

        String[][] mktData = new String[5][2];
        mktData[0][0]= MktDataType.RiskfreeRate.name();
        mktData[0][1] = "0.03";

        mktData[1][0]= MktDataType.DividendRate.name();
        mktData[1][1] = "0.00";

        mktData[2][0]= MktDataType.S0.name();
        mktData[2][1] = "1.0";

        mktData[3][0]= MktDataType.Vol.name();
        mktData[3][1] = "0.2";

        mktData[4][0]= MktDataType.MktDate.name();
        mktData[4][1] = "2020-03-01";

        JqResult jqResult = XlFunc.jqCalc(XlFunc.transpose(table), mktData);

        System.out.println(JsonUtils.writeValueAsString(jqResult));
        assert Math.abs(jqResult.getPv() - 0.049021) < 1.0e-6;
        assert Math.abs(jqResult.getDelta() - (-0.136558)) < 1e-6;
        assert Math.abs(jqResult.getGamma() - (-1.397878)) < 1.0e-6;
        assert Math.abs(jqResult.getRho() - (-0.0000215)) < 1.0e-6;
        assert Math.abs(jqResult.getVega() - (-0.002692)) < 1.0e-6;
        assert Math.abs(jqResult.getTheta() - (0.000092)) < 1.0e-6;
    }


    @Test
    public void validateDoubleBarrierPutMc(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额", "低障碍", "高障碍", "敲出补偿", "敲入基础票息", "参与率"};
        table[1] = new String[]{"DoubleBarrierOption", "2020-02-28", "2021-02-28", "1.05", "Act365", "SH000300", "2021-03-01", "MonteCarlo", "看跌", "人民币", "1.0", "0.75", "1.25", "0.00", "0.00", "1.0"};

        String[][] mktData = new String[5][2];
        mktData[0][0]= MktDataType.RiskfreeRate.name();
        mktData[0][1] = "0.03";

        mktData[1][0]= MktDataType.DividendRate.name();
        mktData[1][1] = "0.00";

        mktData[2][0]= MktDataType.S0.name();
        mktData[2][1] = "1.0";

        mktData[3][0]= MktDataType.Vol.name();
        mktData[3][1] = "0.2";

        mktData[4][0]= MktDataType.MktDate.name();
        mktData[4][1] = "2020-02-28";

        JqResult jqResult = XlFunc.jqCalc(XlFunc.transpose(table), mktData);

        System.out.println(JsonUtils.writeValueAsString(jqResult));
        assert Math.abs(jqResult.getPv() - 0.049021) < 1e-3;
    }


    @Test
    public void validateDoubleBarrierCallMc(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额", "低障碍", "高障碍", "敲出补偿", "敲入基础票息", "参与率"};
        table[1] = new String[]{"DoubleBarrierOption", "2020-02-28", "2021-02-28", "1.05", "Act365", "SH000300", "2021-03-01", "MonteCarlo", "看涨", "人民币", "1.0", "0.75", "1.25", "0.00", "0.00", "1.00"};

        String[][] mktData = new String[5][2];
        mktData[0][0]= MktDataType.RiskfreeRate.name();
        mktData[0][1] = "0.03";

        mktData[1][0]= MktDataType.DividendRate.name();
        mktData[1][1] = "0.00";

        mktData[2][0]= MktDataType.S0.name();
        mktData[2][1] = "1.0";

        mktData[3][0]= MktDataType.Vol.name();
        mktData[3][1] = "0.2";

        mktData[4][0]= MktDataType.MktDate.name();
        mktData[4][1] = "2020-02-28";

        JqResult jqResult = XlFunc.jqCalc(XlFunc.transpose(table), mktData);

        System.out.println(JsonUtils.writeValueAsString(jqResult));
        assert Math.abs(jqResult.getPv() - 0.010845) < 1.0e-3;
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
