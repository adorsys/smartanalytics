package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.config.ConfigStatus;
import de.adorsys.smartanalytics.core.StatusService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@UserResource
@RequestMapping(path = "/status")
public class ConfigStatusController {

    @Autowired
    private StatusService statusService;

    @ApiOperation(
            value = "Read smartanalytics configuration state",
            authorizations = {
                    @Authorization(value = "multibanking_auth", scopes = {
                            @AuthorizationScope(scope = "openid", description = "")
                    })})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<ConfigStatus> status() {
        return new Resource(statusService.getStatus());
    }


}

