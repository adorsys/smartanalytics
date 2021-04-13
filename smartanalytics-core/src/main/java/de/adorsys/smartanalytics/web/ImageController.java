package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.core.ImageService;
import de.adorsys.smartanalytics.exception.FileUploadException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Images")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "api/v1/images")
public class ImageController {

    private final ImageService imageService;

    @GetMapping(path = "/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public HttpEntity<byte[]> getImage(@PathVariable String imageName) {
        return ResponseEntity.ok()
            .body(imageService.getImage(imageName));

    }

    @Operation(description = "Upload images file", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<Void> uploadImages(@RequestParam MultipartFile imagesFile) {
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
