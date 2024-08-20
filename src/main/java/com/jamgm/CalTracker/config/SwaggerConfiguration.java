package com.jamgm.CalTracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI CalTrackerOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.addServersItem(new Server().url("http://localhost:8080/api").description("Development server (HTTP)"));
        return openAPI
                .info(new Info().title("CalTracker")
                        .description("CalTracker applicatie")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("https://quintor.nl")));
    }

}
