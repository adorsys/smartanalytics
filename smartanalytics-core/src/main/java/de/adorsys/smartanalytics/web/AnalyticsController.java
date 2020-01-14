package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.AnalyticsRequest;
import de.adorsys.smartanalytics.api.AnalyticsResult;
import de.adorsys.smartanalytics.core.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Smartanalytics")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PutMapping
    @Operation(description = "Create analytics", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    public Resource<AnalyticsResult> categorize(@RequestBody AnalyticsRequest request) {
        AnalyticsResult categoryResult = analyticsService.analytics(request);

        return new Resource<>(categoryResult);
    }

}
