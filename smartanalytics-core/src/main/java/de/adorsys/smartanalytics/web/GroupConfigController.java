package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.config.GroupConfig;
import de.adorsys.smartanalytics.core.AnalyticsConfigProvider;
import de.adorsys.smartanalytics.core.BookingGroupsService;
import de.adorsys.smartanalytics.exception.FileUploadException;
import de.adorsys.smartanalytics.exception.ResourceNotFoundException;
import de.adorsys.smartanalytics.pers.api.BookingGroupConfigEntity;
import de.adorsys.smartanalytics.pers.utils.ImportUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@UserResource
@RestController
@RequestMapping(path = "api/v1/config/booking-groups")
public class GroupConfigController {

    @Autowired
    private AnalyticsConfigProvider analyticsConfigProvider;
    @Autowired
    private BookingGroupsService bookingGroupsService;

    @ApiOperation(
            value = "Read booking groups configuration",
            authorizations = {
                    @Authorization(value = "multibanking_auth", scopes = {
                            @AuthorizationScope(scope = "openid", description = "")
                    })})
    @GetMapping
    public Resource<GroupConfig> getBookingGroups() {
        BookingGroupConfigEntity groupConfig = analyticsConfigProvider.getBookingGroupConfig();
        if (groupConfig == null) {
            throw new ResourceNotFoundException(GroupConfig.class, "groups");
        }

        return new Resource(groupConfig);
    }

    @ApiOperation(
            value = "Update booking groups configuration",
            authorizations = {
                    @Authorization(value = "multibanking_auth", scopes = {
                            @AuthorizationScope(scope = "openid", description = "")
                    })})
    @PutMapping
    public HttpEntity<Void> updateBookingGroups(@RequestBody GroupConfig groupsContainer) {
        bookingGroupsService.saveBookingGroups(groupsContainer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(
            value = "Upload booking groups configuration file",
            authorizations = {
                    @Authorization(value = "multibanking_auth", scopes = {
                            @AuthorizationScope(scope = "openid", description = "")
                    })})
    @PostMapping(path = "/upload")
    public HttpEntity<?> uploadBookingGroups(@RequestParam MultipartFile bookingGroupsFile) {
        if (!bookingGroupsFile.isEmpty()) {
            try {
                bookingGroupsService.saveBookingGroups(ImportUtils.importBookingGroups(bookingGroupsFile.getInputStream()));

                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("unable import groups", e);
                throw new FileUploadException(bookingGroupsFile.getOriginalFilename());
            }
        } else {
            throw new FileUploadException("File is empty");
        }
    }
}
