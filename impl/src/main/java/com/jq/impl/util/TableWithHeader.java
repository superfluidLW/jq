package com.jq.impl.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jq.common.deal.Deal;
import com.jq.common.deal.DealType;
import com.jq.common.deal.TradeLabel;
import com.jq.common.deal.equity.DigitalOption;
import com.jq.common.deal.equity.DoubleBarrierOption;
import com.jq.common.deal.equity.RangeAccrual;
import com.jq.common.deal.equity.VanillaOption;

import com.jq.common.market.Security;
import com.jq.common.output.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TableWithHeader {
    private List<String> headers = new ArrayList<>();
    private List<List<String>> rows = new ArrayList<>();

    public TableWithHeader(String[][] table){
        for(int col = 0; col < table[0].length; ++col){
            if(table[0][col] != null)
                headers.add(table[0][col]);
        }

        for(int row = 1; row < table.length; ++row){
            List<String> rowList = new ArrayList<>();
            for(int col = 0; col < table[0].length; ++col){
                rowList.add(table[row][col]);
            }
            rows.add(rowList);
        }
    }

    public List<Deal> toTrades(){

        List<Deal> trades = new ArrayList<>();

        Integer tradeTypeCol = headers.indexOf(TradeLabel.DealType.name());
        if(tradeTypeCol.equals(-1)){
            tradeTypeCol = headers.indexOf(TradeLabel.DealType.getDescription());
        }

        if(tradeTypeCol.equals(-1)){
            log.warn("Cannot find DealType column");
            return trades;
        }

        for(int i = 0; i < rows.size(); ++i){
            String tradeTypeStr = rows.get(i).get(tradeTypeCol);
            if(StringUtils.isEmpty(tradeTypeStr)){
                log.warn("Trade type string value is not found in {}th row.", i);
                continue;
            }
            DealType dealType = JqParser.parseEnum(tradeTypeStr, DealType.class);
            Deal trade = null;
            switch (dealType){
                case VanillaOption:
                    trade = toClass(VanillaOption.class, i);
                    break;
                case RangeAccrual:
                    trade = toClass(RangeAccrual.class, i);
                    break;
                case DigitalOption:
                    trade = toClass(DigitalOption.class, i);
                    break;
                case DoubleBarrierOption:
                    trade = toClass(DoubleBarrierOption.class, i);
                    break;
//                case SFP:
//                    trade = toClass(SFP.class, i);
//                    break;
            }
            if(trade != null){
                trades.add(trade);
            }
        }

        return trades;
    }

    private  <T> T toClass(Class T, int nRow) {
        Constructor<T> ctor;
        try {
            ctor = T.getConstructor();
        } catch (Exception ex) {
            log.warn("Class {} does not have a parameter-less constructor", T.getName());
            return null;
        }
        Field[] fields = T.getFields();
        Map<String, Field> fieldMap = Arrays.stream(fields).collect(Collectors.toMap(e -> e.getName().toLowerCase(), e -> e));
        Map<String, TradeLabel> headerLabelMap = headers.stream().filter(Objects::nonNull).collect(Collectors.toMap(e -> e, e -> JqParser.parseEnum(e, TradeLabel.class)));

        List<String> row = rows.get(nRow);

        T object;
        try {
            object = ctor.newInstance();
        } catch (Exception ex) {
            log.warn("Failed to create an empty instance of class {}", T.getName());
            return null;
        }
        for (int i = 0; i < headers.size(); i++) {
            String header = headers.get(i);
            String val = row.get(i);

            if (fieldMap.containsKey(headerLabelMap.get(header).name().toLowerCase())) {
                Field field = fieldMap.get(headerLabelMap.get(header).name().toLowerCase());
                assignFieldValue(object, field, val);
            }
        }

        return object;
    }

    private void assignFieldValue(Object object, Field field, String val){
        Type genericType = field.getGenericType();
        Object value;
        if(StringUtils.isEmpty(val)){
            return;
        }
        try {
            if (genericType.equals(Double.class)) {
                value = Double.parseDouble(val);
            }
            else if(genericType.equals(Integer.class)){
                value = Integer.parseInt(val);
            }
            else if(genericType.equals(Boolean.class)){
                value = Boolean.parseBoolean(val);
            }
            else if (genericType.equals(String.class)) {
                value = val;
            }
            else if (genericType.equals(LocalDate.class)) {
                value = LocalDate.parse(val, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            else if(genericType.equals(new TypeReference<List<Double>>(){}.getType())){
                List<Double> values = new ArrayList<>();
                for (String valStr:val.split(JqConstant.delim)) {
                    values.add(Double.parseDouble(valStr));
                }
                value = values;
            }
            else if(genericType.equals(Security.class)){
                value = JsonUtils.readValue(val, Security.class);
            }
            else if(genericType.equals(DealType.class)){
                value = JqParser.parseEnum(val, DealType.class);
            }
            else if(genericType instanceof Enum){
                value = JqParser.parseEnum(val, ((Enum<?>) genericType).getDeclaringClass());
            }
            else{
                throw new Exception("Cannot parse trades");
            }

            field.set(object, value);
        }
        catch (Exception ex){
            log.warn("Set value {} of field {} failed.", val, field);
        }
    }
 }
