package com.daniel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package: com.daniel.config
 * @ClassName: SwaggerConfig
 * @Author: daniel
 * @CreateTime: 2021/1/30 11:29
 * @Description:
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${swagger.enable}")
    public boolean enable;

    @Bean
    public Docket createDocket() {
        //使得swagger支持在ui界面动态的输入
        List<Parameter> parameterList =new ArrayList<>();

        ParameterBuilder accessTokenBuilder = new ParameterBuilder();
        ParameterBuilder refreshTokenBuilder = new ParameterBuilder();

        accessTokenBuilder.name("authorization").description("测试接口，能动态传输AccessToken入口")
                .modelRef(new ModelRef("String")).parameterType("header").required(false);
        refreshTokenBuilder.name("refreshToken").description("测试接口，能动态传输RefreshToken入口")
                .modelRef(new ModelRef("String")).parameterType("header").required(false);
        //将入口放入到List中
        parameterList.add(accessTokenBuilder.build());
        parameterList.add(refreshTokenBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.daniel.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(parameterList)//开启全局配置
                .enable(enable);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("AMOW后台权限管理系统")
                .description("后台权限管理系统后端接口文档")
                .termsOfServiceUrl("")
                .version("V1.0")
                .build();
    }
}
