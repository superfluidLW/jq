package com.chendu.jq.common.dayCount;

import com.chendu.jq.common.jqInterface.IDayCount;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Act365 implements IDayCount {
    @Override
    public Double yearFraction(LocalDate startDate, LocalDate endDate) {
        return DAYS.between(startDate, startDate) / 365.0;
    }
}
