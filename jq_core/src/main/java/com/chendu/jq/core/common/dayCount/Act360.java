package com.chendu.jq.core.common.dayCount;

import com.chendu.jq.core.common.jqEnum.DayCountType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Act360 {
    public static Double yearFraction(LocalDate startDate, LocalDate endDate) {
        return DAYS.between(startDate, endDate) / 360.0;
    }
}
