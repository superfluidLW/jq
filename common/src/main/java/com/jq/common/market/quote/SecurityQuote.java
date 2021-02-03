package com.jq.common.market.quote;

import lombok.Data;

@Data
public class SecurityQuote {
    private Double quote;

    public SecurityQuote(Double quote){
        this.quote = quote;
    }
}
