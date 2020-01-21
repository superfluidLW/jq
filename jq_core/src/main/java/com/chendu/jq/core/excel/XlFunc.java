package com.chendu.jq.core.excel;

import com.chendu.jq.core.JqTrade;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.equity.DigitalOption;
import com.chendu.jq.core.equity.DoubleBarrierOption;
import com.chendu.jq.core.equity.Option;
import com.chendu.jq.core.equity.VanillaOption;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.util.JqLog;
import com.chendu.jq.core.util.JsonUtils;
import com.chendu.jq.core.util.TableWithHeader;
import com.exceljava.jinx.ExcelAddIn;
import com.exceljava.jinx.ExcelFunction;
import com.exceljava.jinx.ExcelMacro;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class XlFunc {
    private final ExcelAddIn xl;

    /**
     * A non-default constructor is not required, but if there is one that
     * takes an ExcelAddIn then it will be used when instantiating the class
     * (which is only done if there are non-static methods).
     *
     * @param xl ExcelAddIn object for calling back into Excel and Jinx
     */
    public XlFunc(ExcelAddIn xl) {
        this.xl = xl;
    }

    /**
     * Worksheet function that returns the Jinx version.
     *
     * This is a volatile function so will be recalculated each time
     * the workbook is opened or refreshed.
     *
     * @return Jinx version
     */
    @ExcelFunction(
            value = "getJinxVersion",
            isVolatile = true
    )
    public static String getJinxVersion() {
        Package jinx = Package.getPackage("com.exceljava.jinx");
        return jinx.getImplementationVersion();
    }

    @ExcelFunction(
            value = "getJqProps",
            isVolatile = true
    )
    public static String[][] getJqProps() {
        return JqProp.GetAllProps();
    }

    @ExcelFunction(
            value = "getTemplateTradeData",
            isVolatile = true
    )
    public static String[][] getTemplateTradeData() {
        List<Class> classes = Arrays.asList(VanillaOption.class, DigitalOption.class, DoubleBarrierOption.class);
        List<String[][]> templates = new ArrayList<>();
        int maxRow = 0;
        for(int i = 0; i < classes.size(); ++i){
            try {
                Method method = classes.get(i).getMethod("templateTradeData");
                String[][] template = (String[][])method.invoke(null);
                templates.add(template);
                if(template.length > maxRow){
                    maxRow = template.length;
                }
            }
            catch (Exception ex){
                JqLog.error("Failed to get value of trade label {}", ex.getMessage());

            }
        }

        String[][] result = new String[maxRow][2*classes.size()];
        for(int i = 0; i < templates.size(); ++i)
            for(int j = 0; j < templates.get(i).length; ++j)
                for(int k = 0; k < templates.get(i)[0].length; ++k) {
                    result[j][2*i+k] = templates.get(i)[j][k];
                }

        return result;
    }

    @ExcelFunction(
            value = "jqOptionPayoff",
            isVolatile = true
    )
    public static Double[][] jqOptionPayoff(String[][] labelValue) {
        TableWithHeader twh = new TableWithHeader(transpose(labelValue));
        JqTrade trade = twh.toTrades().get(0);

        return ((Option)trade).payOffChart();
    }

    @ExcelFunction(
            value = "jqXlCalc",
            isVolatile = true
    )
    public static Object[][] jqXlCalc(String[][] labelValue, String[][] mktData) {
        JqResult jqResult = jqCalc(labelValue, mktData);
        return jqResult.toXlArray();
    }

    public static JqResult jqCalc(String[][] labelValue, String[][] mktData) {
        TableWithHeader twh = new TableWithHeader(transpose(labelValue));
        JqTrade trade = twh.toTrades().get(0);
        JqMarket mkt = XlUtil.toJqMarket(trade, mktData);

        return trade.calc(mkt);
    }

    public static String[][] transpose(String[][] matrix){
        int row = matrix.length;
        int col = matrix[0].length;

        String[][] result = new String[col][row];
        for(int i = 0; i < row; ++i)
            for(int j = 0; j < col; ++j)
                result[j][i] = matrix[i][j];

        return result;
    }
}
