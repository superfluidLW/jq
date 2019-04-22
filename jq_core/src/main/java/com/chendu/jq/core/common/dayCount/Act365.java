package com.chendu.jq.core.common.dayCount;

import com.chendu.jq.core.common.jqEnum.DayCountType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Data
public class Act365 extends DayCount implements Serializable {
    public Act365(){
        super();
        dayCountType = DayCountType.Act365;
    }

    @Override
    public Double yearFraction(LocalDate startDate, LocalDate endDate) {
        return DAYS.between(startDate, startDate) / 365.0;
    }
}
