package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.AnalyticsRequest;
import de.adorsys.smartanalytics.api.AnalyticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.adorsys.smartanalytics.core.AnalyticsService;

@RestController
@RequestMapping(path = "api/v1/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @RequestMapping(method = RequestMethod.PUT)
    public Resource<AnalyticsResult> categorize(@RequestBody AnalyticsRequest request) {
        AnalyticsResult categoryResult = analyticsService.analytics(request);

        return new Resource<>(categoryResult);
    }


}
