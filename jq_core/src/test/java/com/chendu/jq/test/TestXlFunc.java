package com.chendu.jq.test;

import com.chendu.jq.core.excel.XlFunc;
import org.junit.Test;

public class TestXlFunc {
  @Test
  public void testTemplateTradeData(){
    String[][] res = XlFunc.getTemplateTradeData();
    System.out.println(res);
  }
}
