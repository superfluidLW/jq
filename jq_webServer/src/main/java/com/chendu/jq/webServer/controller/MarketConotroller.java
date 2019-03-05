package com.chendu.jq.webServer.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "市场信息接口")
@RestController
@RequestMapping("/api/market")
@Slf4j
public class MarketConotroller extends Controller{

}
