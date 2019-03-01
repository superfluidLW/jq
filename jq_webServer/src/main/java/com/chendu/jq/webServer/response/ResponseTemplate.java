package com.chendu.jq.webServer.response;

import com.chendu.jq.core.common.jqEnum.JqMessageCode;
import com.chendu.jq.core.util.JqException;
import lombok.Data;

@Data
public class ResponseTemplate {

    private String code;

    private Object data;

    private String message;

    public static ResponseTemplate returnSuccessful(Object data){
        ResponseTemplate template = new ResponseTemplate();
        template.setCode(JqMessageCode.Succeed.toString());
        template.setData(data);
        return template;
    }

    /**
     *
     * @param exception 错误消息
     * @return
     */
    public static ResponseTemplate returnFail(Exception exception){
        ResponseTemplate template = new ResponseTemplate();

        if(exception instanceof JqException) {
            JqException jqException = (JqException)exception;
            template.setCode(jqException.getJqMessageCode().toString());
            template.setMessage(jqException.getMessage());
        }
        else{
            template.setCode(JqMessageCode.Fatal.toString());
            template.setMessage(JqMessageCode.Fatal.toString());
        }
        return template;
    }
}
