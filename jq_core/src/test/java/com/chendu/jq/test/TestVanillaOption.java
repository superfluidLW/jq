package com.chendu.jq.test;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqEnum.Currency;
import com.chendu.jq.core.equity.VanillaOption;
import com.chendu.jq.core.equity.calculator.analytical.EuropeanVanillaCalculator;
import com.chendu.jq.core.excel.ReferenceData;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.mktObj.JqCurve;
import com.chendu.jq.core.market.mktObj.JqTicker;
import com.chendu.jq.core.market.mktObj.JqTickerInfo;
import com.chendu.jq.core.market.mktObj.JqVol;
import com.chendu.jq.core.util.JsonUtils;
import com.chendu.jq.core.util.TableWithHeader;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TestVanillaOption {
    @Test
    public void printTemplate(){
        JqTicker jqTicker = new JqTicker("SH000300");
        System.out.println(jqTicker.toString());
    }

    @Test
    public void validateCalculation(){
        String[][] table = new String[2][];
        table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额"};
        table[1] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "100.605", "Act360", "SH000300", "2020-02-08", "Analytical", "看涨", "人民币", "1"};
        TableWithHeader twh = new TableWithHeader(table);

        List<JqTrade> vos = twh.toTrades();
        VanillaOption vanillaOption = (VanillaOption)vos.get(0);

        JqMarket jqMarket = new JqMarket();
        jqMarket.setMktDate(LocalDate.of(2019, 04, 15));
        jqMarket.setYieldCurveMap(new HashMap<Currency, JqCurve>(){
            {
                put(vanillaOption.getDomCurrency(), new JqCurve(0.05));
            }
        });
        jqMarket.setDividendCurveMap(new HashMap<JqTicker, JqCurve>(){
            {
                put(vanillaOption.getUnderlyingTicker(), new JqCurve(0.05));
            }
        });
        jqMarket.setVolatilityMap(new HashMap<JqTicker, JqVol>(){
            {
                put(vanillaOption.getUnderlyingTicker(), new JqVol(0.2));
            }
        });
        jqMarket.setTickerMap(new HashMap<JqTicker, JqTickerInfo>(){
            {
                put(vanillaOption.getUnderlyingTicker(), new JqTickerInfo(3500.0));
            }
        });

        EuropeanVanillaCalculator calculator = new EuropeanVanillaCalculator();
        JqResult jqResult = calculator.calc(vanillaOption, jqMarket);
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
