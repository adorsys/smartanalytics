package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.config.ConfigStatus;
import de.adorsys.smartanalytics.core.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Status")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/status")
public class ConfigStatusController {

    private final StatusService statusService;

    @Operation(description = "Read smartanalytics configuration state", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<ConfigStatus> status() {
        return new Resource<>(statusService.getStatus());
    }

}

