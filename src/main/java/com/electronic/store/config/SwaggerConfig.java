package com.electronic.store.config;



import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.core.util.AnnotationsUtils.getInfo;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI();

        openAPI.setInfo(getInfo());
        openAPI.setComponents(getSecurityComponents());
        openAPI.addSecurityItem(getSecurityRequirement());

        return openAPI;
    }

    private Info getInfo() {
        Contact contact = new Contact();
        contact.setName("Pavitra Pandey");
        contact.setUrl("https://www.linkedin.com/in/pavitra-pandey-487031276/");
        contact.setEmail("pavitrapandey77@gmail.com");

        Info info = new Info();
        info.setTitle("Electronic Store Backend");
        info.setDescription("This page contains all the APIs of Electronic Store Application");
        info.setVersion("1.0");
        info.setContact(contact);

        return info;
    }

    private Components getSecurityComponents() {
        Components components = new Components();

        SecurityScheme scheme = new SecurityScheme();
        scheme.setName("bearerAuth");
        scheme.setType(SecurityScheme.Type.HTTP);
        scheme.setScheme("bearer");
        scheme.setBearerFormat("JWT");


        components.addSecuritySchemes("bearerAuth", scheme);

        return components;
    }

    private SecurityRequirement getSecurityRequirement() {
        SecurityRequirement requirement = new SecurityRequirement();
        requirement.addList("bearerAuth");
        return requirement;
    }

}
