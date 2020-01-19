package com.chendu.jq.core;

import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.dayCount.DayCount;
import com.chendu.jq.core.common.jqEnum.Currency;
import com.chendu.jq.core.common.jqEnum.TradeLabel;
import com.chendu.jq.core.common.jqEnum.TradeType;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.market.mktObj.JqCurve;
import com.chendu.jq.core.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public abstract class JqTrade implements Serializable {
    public TradeType tradeType;
    public LocalDate startDate;
    public LocalDate maturityDate;
    public Double notional;
    public DayCount dayCount;
    public Currency domCurrency;
    public Currency fgnCurrency;

    public JqTrade(){

    }

    public boolean isValid(){
        Field[] fields = this.getClass().getFields();

        try{
            Map<String, Field> fieldMap = Arrays.stream(fields).collect(Collectors.toMap(e->e.getName(), e->e));
        }
        catch (Exception ex){
            JqLog.info("Duplicate trade label in class " , this.getClass().getName());
            return false;
        }

        for (Field field:fields
             ) {
            try{
                if(JqParser.enumFromStr(field.getName(), TradeLabel.class) == null){
                    JqLog.warn("Not a valid trade label {}", field.getName());
                }
            }
            catch (Exception ex){
                JqLog.error("Not a valid trade label {}", field.getName());
                return false;
            }
        }

        return true;
    }

    public abstract JqResult calc(JqMarket jqMarket);

    public abstract List<JqCashflow> cashflows(JqMarket jqMarket);

    public static String[][] templateTradeData(Class clazz, JqTrade trade ) {
        Field[] fields = clazz.getFields();
        Map<String, Field> fieldMap = Arrays.stream(fields).collect(Collectors.toMap(f -> f.getName().toLowerCase(), f -> f));
        String[][] template = new String[fields.length][2];
        int i = 0;

        for (String tradelabel:JqTradeLabelSeq.tradeLabelSeq){
            if(fieldMap.containsKey(tradelabel.toLowerCase())){
                template[i][0] = tradelabel;
                try {
                    Object value = fieldMap.get(tradelabel.toLowerCase()).get(trade);
                    Field field = fieldMap.get(tradelabel.toLowerCase());
                    String strVal;
                    if(field.getGenericType().equals(LocalDate.class)){
                        strVal = JqParser.jqDateToStr((LocalDate) value);
                    }
                    else if(field.getGenericType().equals(new TypeReference<List<LocalDate>>(){}.getType())){
                        strVal = "";
                        List<LocalDate> listDates = (List<LocalDate>)value;
                        for (int j = 0; j < listDates.size(); j++) {
                            strVal += JqParser.jqDateToStr(listDates.get(j));
                            if(j < listDates.size()-1){
                                strVal += JqConstant.delim;
                            }
                        }
                    }
                    else {
                        strVal = value == null ? "" : value.toString();
                    }

                    template[i][1] = strVal;
                }
                catch (IllegalAccessException ex){
                    JqLog.error("Failed to get value of trade label {}", tradelabel);
                }

                i ++;
            }
        }
        return template;
    }
}
