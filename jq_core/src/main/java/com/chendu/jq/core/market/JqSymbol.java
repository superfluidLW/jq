package com.chendu.jq.core.market;


import com.chendu.jq.core.common.jqEnum.Venue;
import lombok.Data;

import java.io.Serializable;

@Data
public class JqSymbol implements Serializable {
    private String symbol;
    private Venue venue;
}
