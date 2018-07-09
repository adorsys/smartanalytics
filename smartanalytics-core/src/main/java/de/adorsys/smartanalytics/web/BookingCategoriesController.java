package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.CategoriesContainer;
import de.adorsys.smartanalytics.core.CategoriesService;
import de.adorsys.smartanalytics.exception.FileUploadException;
import de.adorsys.smartanalytics.exception.ResourceNotFoundException;
import de.adorsys.smartanalytics.pers.api.CategoriesContainerEntity;
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
@RequestMapping(path = "api/v1/config/booking-categories")
public class BookingCategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @RequestMapping(method = RequestMethod.GET)
    public Resource<CategoriesContainer> getCategories() {
        CategoriesContainerEntity categories = categoriesService.getCategoriesContainer()
            .orElseThrow(() -> new ResourceNotFoundException(CategoriesContainer.class, "categories"));

        return new Resource(categories);
    }

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Void> updateCategories(@RequestBody CategoriesContainer categoriesContainer) {
        categoriesService.saveCategoriesContainer(categoriesContainer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public HttpEntity<?> uploadCategories(@RequestParam MultipartFile categoriesFile) {
        if (!categoriesFile.isEmpty()) {
            try {
                categoriesService.saveCategoriesContainer(ImportUtils.importCategories(categoriesFile.getInputStream()));

                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("unable import categories", e);
                throw new FileUploadException(categoriesFile.getOriginalFilename());
            }
        } else {
            throw new FileUploadException("File is empty");
        }
    }
}
