package com.chendu.jq.test;

import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqEnum.MktDataType;
import com.chendu.jq.core.excel.XlFunc;
import com.chendu.jq.core.util.JsonUtils;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class TestSfpDigital {

  @Test
  public void test(){
    System.out.println(JsonUtils.writeValueAsString(sfp()));
    System.out.println(JsonUtils.writeValueAsString(combo()));
    System.out.println(JsonUtils.writeValueAsString(comboMc()));
  }

  public JqResult sfp() {
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");

    try {
      String[][] table = new String[2][];
      table[0] = new String[]{"产品类型", "开始日期", "到期日期",  "计息基准", "标的资产编码", "行权日期", "本币币种", "名义面额", "收益说明", "估值方法", "蒙特卡洛样本量", "计算MC希腊值"};
      table[1] = new String[]{"SFP", "2019-02-28", "2020-02-28", "Act365", "SH000300", "2020-02-28", "人民币", "1", "ST>=1.05,0.02;ST>0.85,0.02+(1.05-ST)*0.25;ST<=0.85,0.07", "MonteCarlo", "25000", "false"};

      JqResult jqResult = XlFunc.jqCalc(XlFunc.transpose(table), getMarket());

      return jqResult;
    } catch (Exception ex) {
      return new JqResult();
    }
  }

  public JqResult combo() {
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");

    try {
      String[][] table = new String[4][];
      table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "期权方向", "本币币种", "名义面额", "存款利率"};
      table[1] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "0.85", "Act365", "SH000300", "2020-02-28", "Analytical", "看跌", "人民币", "-0.25", ""};
      table[2] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "1.05", "Act365", "SH000300", "2020-02-28", "Analytical", "看跌", "人民币", "0.25", ""};
      table[3] = new String[]{"Deposit", "2019-02-28", "2020-02-28", "", "Act365", "", "", "", "", "人民币", "0.02", "0"};

      JqResult jqResult = XlFunc.jqCalc(XlFunc.transpose(table), getMarket());

      return jqResult;
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return new JqResult();
    }
  }

  public JqResult comboMc() {
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");

    try {
      String[][] table = new String[4][];
      table[0] = new String[]{"产品类型", "开始日期", "到期日期", "行权价格", "计息基准", "标的资产编码", "行权日期", "估值方法", "蒙特卡洛样本量", "计算MC希腊值", "期权方向", "本币币种", "名义面额", "存款利率"};
      table[1] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "0.85", "Act365", "SH000300", "2020-02-28", "MonteCarlo", "25000", "false", "看跌", "人民币", "-0.25", ""};
      table[2] = new String[]{"VanillaOption", "2019-02-28", "2020-02-28", "1.05", "Act365", "SH000300", "2020-02-28", "MonteCarlo", "25000", "false", "看跌", "人民币", "0.25", ""};
      table[3] = new String[]{"Deposit", "2019-02-28", "2020-02-28", "", "Act365", "", "", "", "", "", "", "人民币", "0.02", "0"};

      JqResult jqResult = XlFunc.jqCalc(XlFunc.transpose(table), getMarket());
      return jqResult;
    } catch (Exception ex) {
      return new JqResult();
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
