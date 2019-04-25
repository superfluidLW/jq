package com.chendu.jq.core.common.dayCount;

import com.chendu.jq.core.common.jqEnum.DayCountType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class DayCount implements Serializable {
    public DayCount(){

    }
    protected DayCountType dayCountType;

    public Double yearFraction(LocalDate startDate, LocalDate endDate){
        return 0.0;
    }

    @Override
    public String toString(){
        return dayCountType.toString();
    }
}
