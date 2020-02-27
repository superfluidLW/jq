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

public class TestRangeAccrual {
    @Test
    public void printTemplate(){
        JqTicker jqTicker = new JqTicker("SH000300");
        System.out.println(jqTicker.toString());

        DayCount dc = new DayCount(DayCountType.Act365);
        System.out.println(dc.toString());
    }

    @Test
    public void validateRangeAccrual(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "计息基准", "标的资产编码", "行权日期", "估值方法", "本币币种", "名义面额", "区间下限", "区间上限", "票息"};
        table[1] = new String[]{"RangeAccrual", "2020-03-01", "2021-03-01", "Act365", "SH000300", "2021-03-01", "Analytical", "人民币", "1.0", "3000.0", "3050.0", "0.025"};

        String[][] mktData = new String[5][2];
        mktData[0][0]= MktDataType.RiskfreeRate.name();
        mktData[0][1] = "0.05";

        mktData[1][0]= MktDataType.DividendRate.name();
        mktData[1][1] = "0.02";

        mktData[2][0]= MktDataType.S0.name();
        mktData[2][1] = "3008.0";

        mktData[3][0]= MktDataType.Vol.name();
        mktData[3][1] = "0.2";

        mktData[4][0]= MktDataType.MktDate.name();
        mktData[4][1] = "2020-03-01";

        JqResult jqResult = XlFunc.jqCalc(XlFunc.transpose(table), mktData);

        System.out.println(JsonUtils.writeValueAsString(jqResult));
        assert Math.abs(jqResult.getPv() - 0.001515) < 1.0e-6;
        assert Math.abs(jqResult.getDelta() - 0.018397) < 2.0e-6;
        assert Math.abs(jqResult.getGamma() - (-0.000315)) < 1.0e-6;
        assert Math.abs(jqResult.getRho() - 0.00013439) < 1.0e-5;
        assert Math.abs(jqResult.getVega() - (-0.006560)) < 1.0e-6;
        assert Math.abs(jqResult.getTheta() - (0.0000981)) < 1.0e-6;
    }

    @Test
    public void validateRangeAccrualMc(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "计息基准", "标的资产编码", "行权日期", "估值方法", "蒙特卡洛样本量", "本币币种", "名义面额", "区间下限", "区间上限", "票息"};
        table[1] = new String[]{"RangeAccrual", "2020-03-01", "2021-03-01", "Act365", "SH000300", "2021-03-01", "MonteCarlo", "50000", "人民币", "1.0", "3000.0", "3050.0", "0.025"};

        String[][] mktData = new String[5][2];
        mktData[0][0]= MktDataType.RiskfreeRate.name();
        mktData[0][1] = "0.05";

        mktData[1][0]= MktDataType.DividendRate.name();
        mktData[1][1] = "0.02";

        mktData[2][0]= MktDataType.S0.name();
        mktData[2][1] = "3008.0";

        mktData[3][0]= MktDataType.Vol.name();
        mktData[3][1] = "0.2";

        mktData[4][0]= MktDataType.MktDate.name();
        mktData[4][1] = "2020-03-01";

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
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "计息基准", "标的资产编码", "行权日期", "估值方法", "本币币种", "名义面额", "区间下限", "区间上限", "票息"};
        table[1] = new String[]{"RangeAccrual", "2020-03-01", "2021-03-01", "Act365", "SH000300", "2021-03-01", "Analytical", "人民币", "1.0", "3000.0", "3050.0", "0.025"};
        TableWithHeader twh = new TableWithHeader(table);

        List<JqTrade> vos = twh.toTrades();
        assert vos.size() == 1;
    }
}
