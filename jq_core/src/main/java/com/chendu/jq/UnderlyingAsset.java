package com.chendu.jq;

import lombok.Data;
import sun.awt.Symbol;

import java.io.Serializable;

@Data
public class UnderlyingAsset implements Serializable {
    private Symbol symbol;
}
