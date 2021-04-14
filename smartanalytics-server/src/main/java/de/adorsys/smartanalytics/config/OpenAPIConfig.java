package de.adorsys.smartanalytics.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.OAUTH2;

@Configuration
@Profile("swagger")
public class OpenAPIConfig {

    @Value("${idp.baseUrl:}")
    private String idpBaseUrl;
    @Value("${info.project.version}")
    private String version;
    @Value("${idp.realm:multibanking}")
    private String realm;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .servers(Collections.singletonList(new Server().url("/")))
            .info(new Info().title("Smartanalytics Rest API")
                .version(version)
                .contact(new Contact().name("Alexander Geist").email("age@adorsys.de").url("https://www.adorsys.de"))
                .license(new License()))
            .externalDocs(new ExternalDocumentation()
                .url("https://github.com/adorsys/smartanalytics"))
            .schemaRequirement("multibanking_auth", securityScheme());
    }

    @Bean
    public OpenApiCustomiser sortPathsAlphabetically() {
        return openApi -> openApi.setPaths(openApi.getPaths()
            .entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v2, Paths::new)));
    }

    private SecurityScheme securityScheme() {
        String tokenEndpoint = String.format("%s/auth/realms/%s/protocol/openid-connect/token", idpBaseUrl, realm);
        String authEndpoint = String.format("%s/auth/realms/%s/protocol/openid-connect/auth", idpBaseUrl,
            realm);
        return new SecurityScheme()
            .type(OAUTH2)
            .in(HEADER)
            .bearerFormat("jwt")
            .flows(new OAuthFlows()
                .authorizationCode(new OAuthFlow()
                    .tokenUrl(tokenEndpoint)
                    .authorizationUrl(authEndpoint)));
    }
}
