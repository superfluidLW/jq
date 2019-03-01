package com.chendu.jq.market;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class JqMarket implements Serializable {

    public JqMarket(String[][] table){

    }


    private Map<JqSymbol, Double> quote;
    private Map<JqSymbol, Double> volatility;
}
