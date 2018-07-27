package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.config.CategoriesTree;
import de.adorsys.smartanalytics.core.AnalyticsConfigProvider;
import de.adorsys.smartanalytics.core.CategoriesService;
import de.adorsys.smartanalytics.exception.FileUploadException;
import de.adorsys.smartanalytics.exception.ResourceNotFoundException;
import de.adorsys.smartanalytics.pers.api.CategoriesTreeEntity;
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
@RequestMapping(path = "api/v1/config/booking-categories")
public class CategoriesController {

    @Autowired
    private AnalyticsConfigProvider analyticsConfigProvider;
    @Autowired
    private CategoriesService categoriesService;

    @ApiOperation(
            value = "Read categories tree",
            authorizations = {
                    @Authorization(value = "multibanking_auth", scopes = {
                            @AuthorizationScope(scope = "openid", description = "")
                    })})
    @GetMapping
    public Resource<CategoriesTree> getCategories() {
        CategoriesTreeEntity categoriesTree = analyticsConfigProvider.getCategoriesContainer();
        if (categoriesTree == null) {
            throw new ResourceNotFoundException(CategoriesTree.class, "categories");
        }

        return new Resource(categoriesTree);
    }

    @ApiOperation(
            value = "Update categories tree",
            authorizations = {
                    @Authorization(value = "multibanking_auth", scopes = {
                            @AuthorizationScope(scope = "openid", description = "")
                    })})
    @PutMapping
    public HttpEntity<Void> updateCategories(@RequestBody CategoriesTree categoriesContainer) {
        categoriesService.saveCategories(categoriesContainer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(
            value = "Upload categories tree file",
            authorizations = {
                    @Authorization(value = "multibanking_auth", scopes = {
                            @AuthorizationScope(scope = "openid", description = "")
                    })})
    @PostMapping(path = "/upload")
    public HttpEntity<?> uploadCategories(@RequestParam MultipartFile categoriesFile) {
        if (!categoriesFile.isEmpty()) {
            try {
                categoriesService.saveCategories(ImportUtils.importCategories(categoriesFile.getInputStream()));

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
