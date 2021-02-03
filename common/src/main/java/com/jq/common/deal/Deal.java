package com.jq.common.deal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jq.common.market.Market;
import com.jq.common.convention.DayCount;
import com.jq.common.output.CashFlow;
import com.jq.common.output.Result;
import com.jq.common.convention.Currency;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Deal implements Serializable {
    public DealType dealType;
    public LocalDate startDate;
    public LocalDate maturityDate;
    public Double notional;
    public DayCount dayCount;
    public Currency domCurrency;
    public Currency fgnCurrency;

    public Deal(){

    }

//    public boolean isValid(){
//        Field[] fields = this.getClass().getFields();
//
//        try{
//            Map<String, Field> fieldMap = Arrays.stream(fields).collect(Collectors.toMap(e->e.getName(), e->e));
//        }
//        catch (Exception ex){
//            log.info("Duplicate trade label in class " , this.getClass().getName());
//            return false;
//        }
//
//        for (Field field:fields
//             ) {
//            try{
//                if(JqParser.enumFromStr(field.getName(), TradeLabel.class) == null){
//                    log.warn("Not a valid trade label {}", field.getName());
//                }
//            }
//            catch (Exception ex){
//                log.error("Not a valid trade label {}", field.getName());
//                return false;
//            }
//        }
//
//        return true;
//    }



    public static String[][] templateTradeData(Class clazz, Deal trade ) {
        Field[] fields = clazz.getFields();
        Map<String, Field> fieldMap = Arrays.stream(fields).collect(Collectors.toMap(f -> f.getName().toLowerCase(), f -> f));
        String[][] template = new String[fields.length][2];
        int i = 0;

//        for (String tradelabel: JqTradeLabelSeq.tradeLabelSeq){
//            if(fieldMap.containsKey(tradelabel.toLowerCase())){
//                template[i][0] = JqParser.enumFromStr(tradelabel, TradeLabel.class).getNameCn();
//                try {
//                    Object value = fieldMap.get(tradelabel.toLowerCase()).get(trade);
//                    Field field = fieldMap.get(tradelabel.toLowerCase());
//                    String strVal;
//                    if(field.getGenericType().equals(LocalDate.class)){
//                        strVal = JqParser.jqDateToStr((LocalDate) value);
//                    }
//                    else if(field.getGenericType().equals(new TypeReference<List<LocalDate>>(){}.getType())){
//                        strVal = "";
//                        List<LocalDate> listDates = (List<LocalDate>)value;
//                        for (int j = 0; j < listDates.size(); j++) {
//                            strVal += JqParser.jqDateToStr(listDates.get(j));
//                            if(j < listDates.size()-1){
//                                strVal += JqConstant.delim;
//                            }
//                        }
//                    }
//                    else {
//                        strVal = value == null ? "" : value.toString();
//                    }
//
//                    template[i][1] = strVal;
//                }
//                catch (IllegalAccessException ex){
//                    JqLog.error("Failed to get value of trade label {}", tradelabel);
//                }
//
//                i ++;
//            }
//        }
        return template;
    }
}
