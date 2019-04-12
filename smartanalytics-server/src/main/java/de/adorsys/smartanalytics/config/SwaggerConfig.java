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
import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.ant;

/**
 * Created by alexg on 10.03.17.
 */
@Configuration
@EnableSwagger2
@Profile("swagger")
public class SwaggerConfig {

    @Value("${idp.baseUrl}")
    private String loginUrl;
    @Value("${info.project.version}")
    private String version;
    @Value("${swagger.client.id:multibanking-client}")
    private String swaggerClientId;
    @Value("${idp.realm:multibanking}")
    private String realm;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(UserResource.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Smartanalytics REST Api")
                .contact(new Contact("Alexander Geist adorsys GmbH & Co. KG", null, "age@adorsys.de"))
                .version(version)
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("multibanking_auth", scopes())))
                .forPaths(PathSelectors.any())
                .build();
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(swaggerClientId)
                .build();
    }

    private SecurityScheme securityScheme() {
        String tokenEndpoint = String.format("%s/auth/realms/%s/protocol/openid-connect/token", loginUrl, realm);
        String tokenRequestEndpoint = String.format("%s/auth/realms/%s/protocol/openid-connect/auth", loginUrl, realm);

        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(new TokenEndpoint(tokenEndpoint, "token"))
                .tokenRequestEndpoint(new TokenRequestEndpoint(tokenRequestEndpoint, swaggerClientId, null))
                .build();

        return new OAuthBuilder()
                .name("multibanking_auth")
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
    }

    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{new AuthorizationScope("openid", "openid connect")};
    }
}
