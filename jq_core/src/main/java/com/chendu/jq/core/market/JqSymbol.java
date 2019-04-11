package com.chendu.jq.core.market;


import com.chendu.jq.core.common.jqEnum.Venue;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class JqSymbol implements Serializable {
    private String symbol;
    private Venue venue;

    public JqSymbol(){

    }

    public JqSymbol(String symbol){
        this.symbol = symbol;
    }
}
