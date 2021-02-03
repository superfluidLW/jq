package com.jq.common.deal;

import lombok.Getter;

public enum TradeLabel {
    DealType("产品类型"),
    StartDate("开始日期"),
    MaturityDate("到期日期"),
    Notional("名义面额"),
    Coupon("票息"),
    DepoRate("存款利率"),
    Settlement("交割规则"),

    Strike("行权价格"),
    ExerciseDate("行权日期"),
    ObserveDates("观察日期"),
    UnderlyingTicker("标的资产编码"),
    ValuationModel("估值方法"),
    NumMcPath("蒙特卡洛样本量"),
    CalcMcGreeks("计算MC希腊值"),
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
    ParticipationRate("参与率"),
    PayOffDefinition("收益说明");

    @Getter
    private String description;

    TradeLabel(String description){
        this.description = description;
    }
}
