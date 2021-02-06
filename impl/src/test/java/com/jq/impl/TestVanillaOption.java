package com.jq.impl;

import com.jq.common.convention.*;
import com.jq.common.deal.Deal;
import com.jq.common.deal.DealType;
import com.jq.common.deal.equity.Option;
import com.jq.common.deal.equity.VanillaOption;
import com.jq.common.market.Market;
import com.jq.common.market.MktDataType;
import com.jq.common.market.Security;
import com.jq.common.market.Volatility;
import com.jq.common.market.curve.SwapCurve;
import com.jq.common.market.quote.SecurityQuote;
import com.jq.common.output.Result;
import com.jq.impl.calculator.equity.VanillaOptionCalculator;
import com.jq.impl.util.JqParser;
import org.junit.Test;

import java.time.LocalDate;

public class TestVanillaOption {
    @Test
    public void validateVanillaCall(){
        String[][] table = new String[2][];
        VanillaOption vo = new VanillaOption();
        vo.setDealType(DealType.VanillaOption);
        vo.setStartDate(LocalDate.of(2019, 02, 28));
        vo.setExerciseDate(LocalDate.of(2020, 02, 28));
        vo.setMaturityDate(LocalDate.of(2020, 02, 28));
        vo.setStrike(100.0);
        vo.setDayCount(DayCount.Act365);
        vo.setOptionDirection(OptionDirection.Call);
        vo.setValuationModel(ValuationModel.Analytical);
        vo.setDomCurrency(Currency.Cny);
        vo.setUnderlying(new Security("300300.SH", Venue.ShExg));
        vo.setNotional(1.0);

        String[][] mktData = new String[4][2];
        mktData[0][0]= MktDataType.RiskfreeRate.name();
        mktData[0][1] = "0.05";

        mktData[1][0]= MktDataType.DividendRate.name();
        mktData[1][1] = "0.02";

        mktData[2][0]= MktDataType.S0.name();
        mktData[2][1] = "102.0";

        mktData[3][0]= MktDataType.Vol.name();
        mktData[3][1] = "0.2";

        VanillaOptionCalculator voCalc = new VanillaOptionCalculator();
        Result result = voCalc.calc(vo, toJqMarket(vo, mktData));
        assert Math.abs(result.getPv() - 10.438019) < 1.0e-6;
        assert Math.abs(result.getDelta() - 0.623858) < 2.0e-6;
        assert Math.abs(result.getGamma() - 0.018036) < 1.0e-6;
        assert Math.abs(result.getRho() - 0.005320) < 1.0e-6;
        assert Math.abs(result.getVega() - 0.375265) < 1.0e-6;
        assert Math.abs(result.getTheta() - (-0.014091)) < 1.0e-6;
    }

    public  Market toJqMarket(Deal trade, String[][] mktData){
        Market market = new Market();
        market.setMktDate(trade.startDate);

        for(int i = 0; i < mktData.length; ++i) {
            MktDataType mdt = JqParser.parseEnum(mktData[i][0], MktDataType.class);

            Double rate = Double.parseDouble(mktData[i][1]);
            if (mdt == MktDataType.RiskfreeRate) {
                market.getYieldCurveMap().put(Currency.Cny, new SwapCurve(Currency.Cny, rate));
            }
            if (trade instanceof Option) {
                if (mdt == MktDataType.DividendRate) {
                    market.getDividendCurveMap().put(((Option) trade).getUnderlying(), new SwapCurve(Currency.Cny, rate));
                }
                if (mdt == MktDataType.S0) {
                    market.getSecurityQuoteMap().put(((Option) trade).getUnderlying(), new SecurityQuote(rate));
                }
                if (mdt == MktDataType.Vol) {
                    market.getVolatilityMap().put(((Option) trade).getUnderlying(), new Volatility(rate));
                }
            }
        }

        return market;
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
//
//        Result result = XlFunc.jqCalc(XlFunc.transpose(table), mktData);
//        assert Math.abs(result.getPv() - 5.580697) < 1.0e-6;
//        assert Math.abs(result.getDelta() - (-0.356341)) < 2.0e-6;
//        assert Math.abs(result.getGamma() - 0.018036) < 1.0e-6;
//        assert Math.abs(result.getRho() - (-0.004193)) < 1.0e-6;
//        assert Math.abs(result.getVega() - 0.375265) < 1.0e-6;
//        assert Math.abs(result.getTheta() - (-0.006538)) < 1.0e-6;
    }


    @Test
    public void validateVanillaCallMc(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "蒙特卡洛样本量", "计算MC希腊值", "期权方向", "本币币种", "名义面额"};
        table[1] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "100.0", "Act365", "SH000300", "2020-02-28", "MonteCarlo", "25000", "false", "看涨", "人民币", "1"};

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

//        Result result = XlFunc.jqCalc(XlFunc.transpose(table), mktData);
//        System.out.println(JsonUtils.writeValueAsString(result));
//        assert Math.abs(result.getPv() - 10.438019) < 0.2;
    }


    @Test
    public void TestTradeConversion(){
        String[][] table = new String[3][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额"};
//        table[1] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "100.605", "Act360", "SZ300000", "2020-02-08", "Analytical", "看涨", "人民币", "1"};
//        table[2] = new String[]{"VanillaOption", "2019-02-28", "2021-02-28", "100.1", "Act365", "SH000001", "2020-02-08", "Analytical", "看涨", "人民币", "1"};
//        TableWithHeader twh = new TableWithHeader(table);
//
//        List<Deal> vos = twh.toTrades();
//        assert vos.size() == 2;
    }
}
