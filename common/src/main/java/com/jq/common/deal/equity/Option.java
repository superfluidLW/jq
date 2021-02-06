package com.jq.common.deal.equity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jq.common.convention.OptionDirection;
import com.jq.common.convention.ValuationModel;
import com.jq.common.deal.Deal;
import com.jq.common.market.Security;
import lombok.Data;

import java.time.LocalDate;
import java.util.LinkedHashMap;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Option extends Deal {
    protected Double strike;
    protected LocalDate exerciseDate;
    protected Security underlying;
    protected ValuationModel valuationModel;
    protected Integer numMcPath;
    protected Boolean calcMcGreeks;
    protected OptionDirection optionDirection;

    public Option(){
        super();
        numMcPath = 5000;
        calcMcGreeks = false;
    }

    public abstract Option bumpMaturity(int offset);

    public abstract Double[][] payOffChart();

    public abstract Double calcPayOff(LinkedHashMap<LocalDate, Double> path);
}
