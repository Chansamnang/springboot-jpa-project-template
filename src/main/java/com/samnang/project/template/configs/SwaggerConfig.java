package com.samnang.project.template.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.samnang.project.template.controllers.v1"))
                .paths(PathSelectors.any())
                .build()
                .produces(getAllProduceContentTypes())
                .globalRequestParameters(requestHeaders())
                .apiInfo(apiInfo())
                .forCodeGeneration(true);
    }

    private List<RequestParameter> requestHeaders() {
        RequestParameterBuilder deviceIdParameterBuilder = new RequestParameterBuilder();
        deviceIdParameterBuilder.name("DeviceId")
                .query(q -> q.defaultValue("f4b49d80-58bf-11ec-bf63-0242ac130002")
                        .model(
                                modelSpecificationBuilder -> modelSpecificationBuilder.scalarModel(ScalarType.STRING)
                        )
                ).in(ParameterType.HEADER).required(true).build();

        RequestParameterBuilder userTypeParameterBuilder = new RequestParameterBuilder();
        userTypeParameterBuilder.name("UserType")
                .query(q -> q.defaultValue("staff")
                        .model(
                                modelSpecificationBuilder -> modelSpecificationBuilder.scalarModel(ScalarType.STRING)
                        )
                ).in(ParameterType.HEADER).required(true).build();

        List<RequestParameter> headerParameters = new ArrayList<>();
        headerParameters.add(userTypeParameterBuilder.build());
        headerParameters.add(deviceIdParameterBuilder.build());
        return headerParameters;
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private Set<String> getAllProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        // Add other media types if required in future
        produces.add("application/json");
        produces.add("application/xml");
        return produces;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Springboot JPA Project Template",
                "Template Project.",
                "1.0.0",
                "Terms of service",
                new Contact("Samnang", "www.samnang.com", "limchansamnang@gmail.com"),
                "License of API",
                "API license URL",
                Collections.emptyList()
        );
    }

}