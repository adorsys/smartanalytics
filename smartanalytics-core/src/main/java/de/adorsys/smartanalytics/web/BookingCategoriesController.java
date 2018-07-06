package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.CategoriesContainer;
import de.adorsys.smartanalytics.exception.InvalidCategoriesException;
import de.adorsys.smartanalytics.pers.spi.BookingCategoryRepositoryIf;
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
@RequestMapping(path = "api/v1/categories")
public class BookingCategoriesController {

    @Autowired
    private BookingCategoryRepositoryIf bookingCategoryRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Resource<CategoriesContainer> getCategories() {
        return new Resource(bookingCategoryRepository.getCategoriesContainer());
    }

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Void> updateCategories(@RequestBody CategoriesContainer categoriesContainer) {
        bookingCategoryRepository.saveCategoriesContainer(categoriesContainer);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public HttpEntity<?> uploadCategories(@RequestParam MultipartFile categoriesFile) {
        if (!categoriesFile.isEmpty()) {
            try {
                bookingCategoryRepository.saveCategoriesContainer(ImportUtils.importCategories(categoriesFile.getInputStream()));

                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("unable import categories", e);
                throw new InvalidCategoriesException(categoriesFile.getOriginalFilename());
            }
        } else {
            throw new InvalidCategoriesException("File is empty");
        }
    }
}
