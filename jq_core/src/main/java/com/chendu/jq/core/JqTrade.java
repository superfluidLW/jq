package com.chendu.jq.core;

import com.chendu.jq.core.common.JqCashflow;
import com.chendu.jq.core.common.JqResult;
import com.chendu.jq.core.common.jqEnum.Currency;
import com.chendu.jq.core.common.jqEnum.TradeLabel;
import com.chendu.jq.core.common.jqEnum.TradeType;
import com.chendu.jq.core.market.JqMarket;
import com.chendu.jq.core.util.JqLog;
import com.chendu.jq.core.util.JqParser;
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
                    JqLog.warn("Not a valid trade label", field.getName());
                }
            }
            catch (Exception ex){
                JqLog.error("Not a valid trade label", field.getName());
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
