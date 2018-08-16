package de.adorsys.smartanalytics.config;

import de.adorsys.smartanalytics.web.UserResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.ant;

/**
 * Created by alexg on 10.03.17.
 */
@Configuration
@EnableSwagger2
@Profile("swagger")
public class SwaggerConfig {

    @Value("${swagger.login.url}")
    private String loginUrl;
    @Value("${info.project.version}")
    private String version;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("Smartanalytics REST Api")
                        .contact(new Contact("Alexander Geist adorsys GmbH & Co. KG", null, "age@adorsys.de"))
                        .version(version)
                        .build())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(UserResource.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(oauth()))
                .securityContexts(Arrays.asList(securityContext()));
    }

    @Bean
    public SecurityContext securityContext() {
        AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = new AuthorizationScope("openid", "openid connect");

        SecurityReference securityReference = SecurityReference.builder()
                .reference("multibanking_auth")
                .scopes(scopes)
                .build();

        return SecurityContext.builder()
                .securityReferences(Arrays.asList(securityReference))
                .forPaths(ant("*"))
                .build();
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .appName("Smartanalytics swagger client")
                .realm("multibanking")
                .clientId("multibanking-client")
                .build();
    }

    @Bean
    public SecurityScheme oauth() {
        return new OAuthBuilder()
                .name("multibanking_auth")
                .scopes(Arrays.asList(new AuthorizationScope("openid", "openid connect")))
                .grantTypes(grantTypes())
                .build();
    }

    private List<GrantType> grantTypes() {
        GrantType grantType = new ImplicitGrantBuilder()
                .loginEndpoint(new LoginEndpoint(loginUrl))
                .build();
        return Arrays.asList(grantType);
    }
}
