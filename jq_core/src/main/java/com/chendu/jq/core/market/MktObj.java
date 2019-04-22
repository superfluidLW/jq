package com.chendu.jq.core.market;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class MktObj implements Serializable {
    protected String id;
}
