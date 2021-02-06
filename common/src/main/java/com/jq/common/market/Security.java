package com.jq.common.market;

import com.jq.common.convention.Venue;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class Security extends MktData implements Serializable {
    private Venue venue;
    private String symbol;
    public Security(){
    }

    public Security(String symbol, Venue venue){
        this.symbol = symbol;
        this.venue = venue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Security that = (Security) o;
        return symbol .equals(that.symbol) && venue.equals(that.venue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, venue);
    }

    @Override
    public String toString(){
        return symbol;
    }
}
