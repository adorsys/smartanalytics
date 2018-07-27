package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.AnalyticsRequest;
import de.adorsys.smartanalytics.api.AnalyticsResult;
import de.adorsys.smartanalytics.core.AnalyticsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@UserResource
@RequestMapping(path = "api/v1/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @PutMapping
    @ApiOperation(
            value = "Create analytics",
            authorizations = {
                    @Authorization(value = "multibanking_auth", scopes = {
                            @AuthorizationScope(scope = "openid", description = "")
                    })})
    public Resource<AnalyticsResult> categorize(@RequestBody AnalyticsRequest request) {
        AnalyticsResult categoryResult = analyticsService.analytics(request);

        return new Resource<>(categoryResult);
    }


}
