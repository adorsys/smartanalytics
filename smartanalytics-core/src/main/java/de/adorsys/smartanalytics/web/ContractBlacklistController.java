package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.config.ContractBlacklist;
import de.adorsys.smartanalytics.core.AnalyticsConfigProvider;
import de.adorsys.smartanalytics.core.ContractBlacklistService;
import de.adorsys.smartanalytics.exception.FileUploadException;
import de.adorsys.smartanalytics.exception.ResourceNotFoundException;
import de.adorsys.smartanalytics.pers.api.ContractBlacklistEntity;
import de.adorsys.smartanalytics.pers.utils.ImportUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Contract blacklist")
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "api/v1/config/contract-blacklist")
public class ContractBlacklistController {

    private final AnalyticsConfigProvider analyticsConfigProvider;
    private final ContractBlacklistService contractBlacklistService;

    @Operation(description = "Read contract blacklist configuration", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @GetMapping
    public Resource<ContractBlacklist> getContractBlackList() {
        ContractBlacklistEntity contractBlacklist = analyticsConfigProvider.getContractBlacklist();
        if (contractBlacklist == null) {
            throw new ResourceNotFoundException(ContractBlacklist.class, "contract-blacklist");
        }

        return new Resource<>(contractBlacklist);
    }

    @Operation(description = "Update contract blacklist configuration", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @PutMapping
    public HttpEntity<Void> updateContractBlackList(@RequestBody ContractBlacklist contractBlacklist) {
        contractBlacklistService.saveContractBlacklist(contractBlacklist);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(description = "Upload contract blacklist configuration file", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<Void> uploadContractBlackList(@RequestParam MultipartFile contractBlacklistFile) {
        if (!contractBlacklistFile.isEmpty()) {
            try {
                contractBlacklistService.saveContractBlacklist(ImportUtils.readAsYaml(contractBlacklistFile.getInputStream(), ContractBlacklist.class));

                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("unable import groups", e);
                throw new FileUploadException(contractBlacklistFile.getOriginalFilename());
            }
        } else {
            throw new FileUploadException("File is empty");
        }
    }
}
