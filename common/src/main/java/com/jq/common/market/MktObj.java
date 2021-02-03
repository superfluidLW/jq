package com.jq.common.market;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class MktObj implements Serializable {
    protected String id;
}
