package com.chendu.jq.webServer.controller;

import com.chendu.jq.webServer.request.ValueSingleTradeRequest;
import com.chendu.jq.webServer.response.ResponseTemplate;
import com.chendu.jq.webServer.util.DefferredResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;

@Api(description = "计算接口")
@RestController
@RequestMapping("/api/valuation")
@Slf4j
public class ValuationConotroller extends Controller{

    @ApiOperation(value = "估值单笔交易")
    @PostMapping(value = "/valueSingleTrade")
    public DeferredResult<ResponseTemplate> valueTrade(@RequestBody ValueSingleTradeRequest valueSingleTradeRequest){
        log.info("Starting to value single trade");
        DefferredResponseResult responseResult = new DefferredResponseResult(timeoutSeconds);

        executorService.execute(() -> {
            ResponseTemplate responseTemplate = ResponseTemplate.returnSuccessful(100);

            responseResult.setResult(responseTemplate);
        });

        return responseResult.getResult();
    }
}
