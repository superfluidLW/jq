package com.chendu.jq.webServer.util;

import com.chendu.jq.core.common.jqEnum.JqMessageCode;
import com.chendu.jq.core.util.JqException;
import com.chendu.jq.webServer.response.ResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.async.DeferredResult;

@Slf4j
public class DefferredResponseResult {

    private DeferredResult<ResponseTemplate> result = null;

    public DefferredResponseResult(Integer timeoutSeconds){
        this.result = new DeferredResult(timeoutSeconds * 1000L);//处理超时事件 采用委托机制
        result.onTimeout(new Runnable() {
            @Override
            public void run() {
                log.error("DeferredResult超时");
                result.setResult(ResponseTemplate.returnFail(new JqException(JqMessageCode.TimeOut, "")));
            }
        });
        result.onCompletion(new Runnable() {
            @Override
            public void run() {
                //完成后
                log.info("调用完成");
            }
        });
        result.onError(ex-> {
            ex.printStackTrace();
            //完成后
            log.info("request error {}", ex.getMessage());
            result.setResult(ResponseTemplate.returnFail(new JqException(JqMessageCode.Fatal, "")));
        });
    }

    public void setResult(ResponseTemplate responseTemplate){
        this.result.setResult(responseTemplate);
    }

    public DeferredResult<ResponseTemplate> getResult(){
        return result;
    }
}
