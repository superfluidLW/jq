package com.chendu.jq.core.market.mktObj;

import com.chendu.jq.core.common.jqEnum.Venue;
import com.chendu.jq.core.market.MktObj;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class JqTicker extends MktObj implements Serializable {
    private Venue venue;

    public JqTicker(){

    }

    public JqTicker(String id){
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JqTicker that = (JqTicker) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
