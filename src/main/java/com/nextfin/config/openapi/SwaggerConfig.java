package com.nextfin.config.openapi;

import com.nextfin.AppConstants;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customSwaggerConfig() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development Server");

        return new OpenAPI().info(new Info().title("AA Banking App API")
                                            .version(AppConstants.API_VERSION)
                                            .description(
                                                    "RESTful API for Financial Transactions & Basic Banking Operations"))
                            .servers(
                                    List.of(server));
    }
}
