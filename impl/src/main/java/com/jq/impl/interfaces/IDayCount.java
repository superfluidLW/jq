package com.jq.impl.interfaces;

import java.time.LocalDate;

public interface IDayCount {
    Double yearFraction(LocalDate startDate, LocalDate endDate);
}
