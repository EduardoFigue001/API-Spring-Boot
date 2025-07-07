package com.ferremas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI ferremasApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ferremas API")
                        .description("API RESTful para la gestión de productos de ferretería Ferremas. " +
                                   "Esta API permite consultar información detallada de productos, " +
                                   "incluyendo precios, modelos, marcas, códigos y stock.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo Ferremas")
                                .email("soporte@ferremas.cl")
                                .url("https://www.ferremas.cl"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url("https://api.ferremas.cl")
                                .description("Servidor de Producción")
                ));
    }
}
