package com.chendu.jq.core.common.jqEnum;

import lombok.Getter;

public enum   TradeLabel {
    TradeType("产品类型"),
    StartDate("开始日期"),
    ExerciseDate("行权日期"),
    MaturityDate("到期日期"),
    Notional("名义面额"),
    ObserveDates("观察日期"),
    UnderlyingTicker("标的资产编码"),
    ValuationModel("估值方法"),
    NumMcPath("蒙特卡洛样本量"),
    CalcMcGreeks("计算MC希腊值"),
    Strike("行权价格"),
    DayCount("计息基准"),
    DigitalPayoff("二值期权收益"),
    OptionDirection("期权方向"),
    DomCurrency("本币币种"),
    FgnCurrency("外币币种"),
    LBarrier("低障碍"),
    UBarrier("高障碍"),
    KoRebate("敲出补偿"),
    KiCoupon("敲入基础票息"),
    LRange("区间下限"),
    URange("区间上限"),
    Coupon("票息");

    @Getter
    private String nameCn;

    TradeLabel(String nameCn){
        this.nameCn = nameCn;
    }
}
