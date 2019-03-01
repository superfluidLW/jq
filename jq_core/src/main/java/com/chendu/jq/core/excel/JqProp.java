package com.chendu.jq.core.excel;

import com.chendu.jq.core.common.jqEnum.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class JqProp {
    public static String[][] GetAllProps(){
        List<Class> enumClz = Arrays.asList(BusinessDayConvention.class, Currency.class, DayCount.class, Frequency.class, OptionDirection.class, Stub.class, TradeLabel.class, TradeType.class, ValuationModel.class, Venue.class);

        int maxLen = 0;
        for (Class cla:enumClz){
            int len = cla.getFields().length;
            if(len > maxLen) maxLen = len;
        }
        String[][] result = new String[maxLen+1][enumClz.size()];

        for(int i = 0; i < maxLen+1; i++)
            for(int j = 0; j < enumClz.size(); j++)
                result[i][j]="";

        for(int col = 0; col < enumClz.size(); ++col){
            Field[] fields = enumClz.get(col).getFields();
            result[0][col]=enumClz.get(col).getSimpleName();
            for(int row= 0; row < fields.length; ++row){
                result[row+1][col] = fields[row].getName();
            }
        }

        return result;
    }
}
