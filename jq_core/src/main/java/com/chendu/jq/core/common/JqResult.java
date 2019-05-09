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

    public String[][] toStringArray(){
        String[][] result = new String[6][2];
        result[0][0] = "pv";
        result[0][1] = pv.toString();

        result[1][0] = "delta";
        result[1][1] = delta.toString();

        result[2][0] = "gamma";
        result[2][1] = gamma.toString();

        result[3][0] = "vega";
        result[3][1] = vega.toString();

        result[4][0] = "rho";
        result[4][1] = rho.toString();

        result[5][0] = "theta";
        result[5][1] = theta.toString();

        return result;
    }
}
