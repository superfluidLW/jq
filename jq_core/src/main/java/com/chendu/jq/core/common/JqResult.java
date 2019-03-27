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
}
