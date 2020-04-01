package com.chendu.jq.core.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class JqResult implements Serializable {
    private Double pv=0.0;
    public Double delta=0.0;
    public Double gamma=0.0;
    public Double vega=0.0;
    public Double rho=0.0;
    public Double theta=0.0;

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

    public void merge(JqResult other){
        pv += other.pv;
        delta += other.delta;
        gamma += other.gamma;
        vega += other.vega;
        rho += other.rho;
        theta += other.theta;
    }
}
