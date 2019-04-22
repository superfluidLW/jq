package com.chendu.jq.core.common.dayCount;

import com.chendu.jq.core.common.jqEnum.DayCountType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

@Data
public class Act360 extends DayCount implements Serializable {
    public Act360(){
        super();
        dayCountType = DayCountType.Act360;
    }

    @Override
    public Double yearFraction(LocalDate startDate, LocalDate endDate) {
        return DAYS.between(startDate, startDate) / 360.0;
    }
}
