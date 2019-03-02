package com.chendu.jq.webServer.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="单笔交易估值计算")
public class ValueSingleTradeRequest implements Serializable {

    @ApiModelProperty(value="交易信息",name="tradeInfo",example="SampleTradeInfo",required=true)
    private String tradeInfo;
}
