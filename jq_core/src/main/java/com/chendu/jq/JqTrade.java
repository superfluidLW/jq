package com.chendu.jq;

import com.chendu.jq.common.JqCashflow;
import com.chendu.jq.common.JqResult;
import com.chendu.jq.common.jqEnum.TradeLabel;
import com.chendu.jq.common.jqEnum.TradeType;
import com.chendu.jq.market.JqMarket;
import com.chendu.jq.util.JqParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Data
public abstract class JqTrade implements Serializable {
    public TradeType tradeType;
    public LocalDate startDate;
    public LocalDate maturityDate;

    public JqTrade(){

    }

    public boolean isValid(){
        Field[] fields = this.getClass().getFields();

        try{
            Map<String, Field> fieldMap = Arrays.stream(fields).collect(Collectors.toMap(e->e.getName(), e->e));
        }
        catch (Exception ex){
            log.warn("Duplicate trade label in class {}", this.getClass().getName());
            return false;
        }

        for (Field field:fields
             ) {
            try{
                if(JqParser.enumFromStr(field.getName(), TradeLabel.class) == null){
                    System.out.println(String.format("%s is not a valid trade label", field.getName()));
                }
            }
            catch (Exception ex){
                System.out.println(String.format("%s is not a valid trade label", field.getName()));
                return false;
            }
        }

        return true;
    }

    public abstract JqResult calc(JqMarket jqMarket);

    public abstract List<JqCashflow> getCashflow(JqMarket jqMarket);

    public List<JqCashflow> getReplicatingCashflow(JqMarket jqMarket){
        return getCashflow(jqMarket);
    }
}
