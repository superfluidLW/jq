package com.chendu.jq.core.market;

public abstract class BumpableMktObj extends MktObj {
    protected abstract Double bumpUp();
    protected abstract Double bumpDown();
}
