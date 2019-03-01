package com.chendu.jq.core.common.jqInterface;

import java.time.LocalDate;

public interface IDayCount {
    Double yearFraction(LocalDate startDate, LocalDate endDate);
}
