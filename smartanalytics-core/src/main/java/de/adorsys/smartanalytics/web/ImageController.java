package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.core.ImageService;
import de.adorsys.smartanalytics.exception.FileUploadException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@UserResource
@RestController
@RequestMapping(path = "api/v1/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping(path = "/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public HttpEntity<byte[]> getImage(@PathVariable String imageName) {
        return ResponseEntity.ok()
                .body(imageService.getImage(imageName));

    }

    @ApiOperation(
            value = "Upload images file",
            authorizations = {
                    @Authorization(value = "multibanking_auth", scopes = {
                            @AuthorizationScope(scope = "openid", description = "")
                    })})
    @PostMapping(path = "/upload")
    public HttpEntity<?> uploadImages(@RequestParam MultipartFile imagesFile) {
        if (!imagesFile.isEmpty()) {
            try {
                imageService.importImages(imagesFile.getInputStream());

                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("unable import images", e);
                throw new FileUploadException(imagesFile.getOriginalFilename());
            }
        } else {
            throw new FileUploadException("File is empty");
        }
    }
}
