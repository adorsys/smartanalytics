package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.BookingGroupConfig;
import de.adorsys.smartanalytics.core.BookingGroupsService;
import de.adorsys.smartanalytics.exception.FileUploadException;
import de.adorsys.smartanalytics.exception.ResourceNotFoundException;
import de.adorsys.smartanalytics.pers.api.BookingGroupConfigEntity;
import de.adorsys.smartanalytics.pers.utils.ImportUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(path = "api/v1/config/booking-groups")
public class BookingGroupsController {

    @Autowired
    private BookingGroupsService bookingGroupsService;

    @RequestMapping(method = RequestMethod.GET)
    public Resource<BookingGroupConfig> getBookingGroups() {
        BookingGroupConfigEntity groups = bookingGroupsService.getBookingGroups()
                .orElseThrow(() -> new ResourceNotFoundException(BookingGroupConfig.class, "groups"));

        return new Resource(groups);
    }

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Void> updateBookingGroups(@RequestBody BookingGroupConfig groupsContainer) {
        bookingGroupsService.saveBookingGroups(groupsContainer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
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
