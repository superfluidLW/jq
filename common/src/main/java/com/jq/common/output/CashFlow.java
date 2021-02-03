package com.jq.common.output;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class CashFlow implements Serializable {
    private LocalDate accBegDate;
    private LocalDate accEndDate;
    private LocalDate refEndDate;
    private LocalDate payDate;
    private Double payAmount;
    private Double df;
    private Double begPrincipal;


}
