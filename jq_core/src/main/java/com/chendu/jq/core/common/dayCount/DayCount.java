package com.chendu.jq.core.common.dayCount;

import com.chendu.jq.core.common.jqEnum.DayCountType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class DayCount implements Serializable {
    public DayCount(){

    }

    public DayCount(DayCountType dayCountType){
        this.dayCountType = dayCountType;
    }

    protected DayCountType dayCountType;

    public Double yearFraction(LocalDate startDate, LocalDate endDate){
        switch (dayCountType){
            case Act360:
                return Act360.yearFraction(startDate, endDate);
            case Act365:
            default:
                return Act365.yearFraction(startDate, endDate);
        }
    }

    @Override
    public String toString(){
        return dayCountType.name();
    }
}
