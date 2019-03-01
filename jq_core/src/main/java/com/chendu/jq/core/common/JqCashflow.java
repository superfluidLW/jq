package com.chendu.jq.core.common;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class JqCashflow implements Serializable {
    private LocalDate accStartDate;
    private LocalDate accEndDate;
    private LocalDate refEndDate;
    private LocalDate payDate;
    private Double payAmount;
    private Double df;
    private Double begPrincipal;


}
