package com.chendu.jq.webServer.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "交易信息接口")
@RestController
@RequestMapping("/api/trade")
@Slf4j
public class TradeConotroller extends Controller{

}
