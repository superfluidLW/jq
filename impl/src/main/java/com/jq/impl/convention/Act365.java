package com.jq.impl.convention;

import com.jq.impl.interfaces.IDayCount;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Act365 implements IDayCount {
    @Override
    public Double yearFraction(LocalDate startDate, LocalDate endDate) {
        return DAYS.between(startDate, endDate) / 365.0;
    }
}
