package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.config.ConfigStatus;
import de.adorsys.smartanalytics.core.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/status")
public class ConfigStatusController {

    @Autowired
    private StatusService statusService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource<ConfigStatus> status() {
        return new Resource(statusService.getStatus());
    }


}

