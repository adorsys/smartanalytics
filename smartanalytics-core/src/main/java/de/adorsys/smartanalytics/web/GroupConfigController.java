package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.config.GroupConfig;
import de.adorsys.smartanalytics.core.AnalyticsConfigProvider;
import de.adorsys.smartanalytics.core.BookingGroupsService;
import de.adorsys.smartanalytics.exception.FileUploadException;
import de.adorsys.smartanalytics.exception.ResourceNotFoundException;
import de.adorsys.smartanalytics.pers.api.BookingGroupConfigEntity;
import de.adorsys.smartanalytics.pers.utils.ImportUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Groups config")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "api/v1/config/booking-groups")
public class GroupConfigController {

    private final AnalyticsConfigProvider analyticsConfigProvider;
    private final BookingGroupsService bookingGroupsService;

    @Operation(description = "Read booking groups configuration", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @GetMapping
    public Resource<GroupConfig> getBookingGroups() {
        BookingGroupConfigEntity groupConfig = analyticsConfigProvider.getBookingGroupConfig();
        if (groupConfig == null) {
            throw new ResourceNotFoundException(GroupConfig.class, "groups");
        }

        return new Resource<>(groupConfig);
    }

    @Operation(description = "Update booking groups configuration", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @PutMapping
    public HttpEntity<Void> updateBookingGroups(@RequestBody GroupConfig groupsContainer) {
        bookingGroupsService.saveBookingGroups(groupsContainer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(description = "Upload booking groups configuration file", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @PostMapping(path = "/upload")
    public HttpEntity<Void> uploadBookingGroups(@RequestParam MultipartFile bookingGroupsFile) {
        if (!bookingGroupsFile.isEmpty()) {
            try {
                bookingGroupsService.saveBookingGroups(ImportUtils.readAsYaml(bookingGroupsFile.getInputStream(), GroupConfig.class));

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
