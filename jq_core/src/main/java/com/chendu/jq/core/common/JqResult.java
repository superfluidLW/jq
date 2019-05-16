package com.chendu.jq.core.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class JqResult implements Serializable {
    private Double pv;
    public Double delta;
    public Double gamma;
    public Double vega;
    public Double rho;
    public Double theta;

    public Double[][] toXlArray(){
        Double[][] result = new Double[6][1];
        result[0][0] = pv;
        result[1][0] = delta;
        result[2][0] = gamma;
        result[3][0] = vega;
        result[4][0] = rho;
        result[5][0] = theta;

        return result;
    }
}
