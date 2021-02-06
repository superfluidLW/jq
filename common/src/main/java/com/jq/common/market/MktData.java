package com.jq.common.market;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class MktData implements Serializable {
    protected String id;
}
