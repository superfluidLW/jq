package com.chendu.jq.test;

import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqEnum.MktDataType;
import com.chendu.jq.core.equity.SFP;
import com.chendu.jq.core.excel.XlFunc;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.time.LocalDate;
import java.util.LinkedHashMap;

public class TestSfpBarrier {
  @Test
  public void testSfp(){
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine=mgr.getEngineByName("JavaScript");

    try {
      SFP sfp = new SFP();
      LinkedHashMap<LocalDate, Double> path =new LinkedHashMap<>();
      sfp.setExerciseDate(LocalDate.now().plusDays(1));
      path.put(LocalDate.now(), 102.0);
      path.put(LocalDate.now().plusDays(1), 103.0);
      sfp.setPayOffDefinition("S<0.96,0.0435;ST<=1.0,0.005+5.1×min(0.04, ST);ST>1.0,0.005");
      sfp.calcPayOff(path);
      int x = 1;
    }
    catch (Exception ex){

    }
  }



  @Test
  public void sfp() {
    ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");

    try {
      String[][] table = new String[2][];
      table[0] = new String[]{"产品类型", "开始日期", "到期日期",  "计息基准", "标的资产编码", "行权日期", "本币币种", "名义面额", "收益说明", "估值方法", "蒙特卡洛样本量", "计算MC希腊值"};
      table[1] = new String[]{"SFP", "2019-02-28", "2020-02-28", "Act365", "SH000300", "2020-02-28", "人民币", "1", "ST>=1.05,0.02;ST>0.85,0.02+(1.05-ST)*0.25;ST<=0.85,0.07", "MonteCarlo", "2500", "false"};

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
