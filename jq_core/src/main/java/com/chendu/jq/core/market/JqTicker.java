package com.chendu.jq.core.market;


import com.chendu.jq.core.common.jqEnum.Venue;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class JqTicker implements Serializable {
    private String id;
    private Venue venue;

    public JqTicker(){

    }

    public JqTicker(String id){
        this.id = id;
    }
}
