package com.chendu.jq.test;

import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqEnum.MktDataType;
import com.chendu.jq.core.excel.XlFunc;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class TestSfpVanilla {
  @Test
  public void sfp() {
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");

    try {
      String[][] table = new String[3][];
      table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额"};
      table[1] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "0.85", "Act365", "SH000300", "2020-02-28", "Analytical", "看涨", "人民币", "0.25"};
      table[2] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "1.05", "Act365", "SH000300", "2020-02-28", "Analytical", "看跌", "人民币", "-0.25"};
      table[2] = new String[]{"Deposit", "2019-02-28", "2020-02-28", "1.05", "Act365", "SH000300", "2020-02-28", "Analytical", "看跌", "人民币", "-0.25"};

      JqResult jqResult = XlFunc.jqCalc(XlFunc.transpose(table), getMarket());

      int x = 1;
    } catch (Exception ex) {

    }
  }

  public String[][] getMarket(){
    String[][] mktData = new String[5][2];
    mktData[0][0]= MktDataType.RiskfreeRate.name();
    mktData[0][1] = "0.05";

    mktData[1][0]= MktDataType.DividendRate.name();
    mktData[1][1] = "0.02";

    mktData[2][0]= MktDataType.S0.name();
    mktData[2][1] = "1.0";

    mktData[3][0]= MktDataType.Vol.name();
    mktData[3][1] = "0.2";

    mktData[4][0]= MktDataType.MktDate.name();
    mktData[4][1] = "2019-02-28";
    return mktData;
  }
}
