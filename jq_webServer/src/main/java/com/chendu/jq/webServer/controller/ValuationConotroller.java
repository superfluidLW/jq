package com.chendu.jq.webServer.controller;

import com.chendu.jq.webServer.response.ResponseTemplate;
import com.chendu.jq.webServer.util.DefferredResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@Api(description = "计算接口")
@RestController
@RequestMapping("/api/valuation")
@Slf4j
public class ValuationConotroller extends Controller{

    @ApiOperation(value = "估值")
    @PostMapping(value = "/valueTrade")
    public DeferredResult<ResponseTemplate> valueTrade(){
        DefferredResponseResult responseResult = new DefferredResponseResult(timeoutSeconds);

        executorService.execute(() -> {
            ResponseTemplate responseTemplate = ResponseTemplate.returnSuccessful(100);

            responseResult.setResult(responseTemplate);
        });

        return responseResult.getResult();
    }
}
