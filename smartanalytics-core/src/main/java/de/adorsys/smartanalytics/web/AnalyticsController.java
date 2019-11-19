package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.AnalyticsRequest;
import de.adorsys.smartanalytics.api.AnalyticsResult;
import de.adorsys.smartanalytics.core.AnalyticsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;

@Api(tags = "Smartanalytics")
@RequiredArgsConstructor
@RestController
@UserResource
@RequestMapping(path = "api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

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
