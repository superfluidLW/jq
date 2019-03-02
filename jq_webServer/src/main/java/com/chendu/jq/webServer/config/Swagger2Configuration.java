package com.chendu.jq.webServer.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
//@ConditionalOnProperty(
//        name = "application.prodMode",
//        havingValue = "false"
//)
public class Swagger2Configuration {

    @Value("${host.name}")
    private String hostName;

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.chendu.jq.webServer.controller"))
                .paths(PathSelectors.any())
                .build();
        if(StringUtils.isNotEmpty(hostName)){
            docket.host(hostName);
        }
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("JQ")
                .description(generateDescription())
                .termsOfServiceUrl("http://github.com/")
                .contact(new Contact("JQ Team","","superfluid_lw@163.com"))
                .version("1.0")
                .build();
    }

    public String generateDescription(){
        StringBuilder builder = new StringBuilder();
        builder.append("用于Web前端与服务端交互");
        builder.append("\n\r公司官网: http://github.com/");
        return builder.toString();
    }

    private List<Parameter> setHeaderToken() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        ParameterBuilder typePar = new ParameterBuilder();
        ParameterBuilder timePar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        typePar.parameterType("header").name("Client-Type").description("pc").modelRef(new ModelRef("string")).required(true).build();
        timePar.parameterType("header").name("Client-Time").description("data").modelRef(new ModelRef("string")).required(true).build();
        tokenPar.name("Authorization").description("token").modelRef(new ModelRef("string")).parameterType("header").required(true).build();
        pars.add(tokenPar.build());
        pars.add(typePar.build());
        pars.add(timePar.build());
        return pars;
    }
}
