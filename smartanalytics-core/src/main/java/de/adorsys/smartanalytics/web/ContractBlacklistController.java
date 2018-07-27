package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.config.ContractBlacklist;
import de.adorsys.smartanalytics.core.AnalyticsConfigProvider;
import de.adorsys.smartanalytics.core.ContractBlacklistService;
import de.adorsys.smartanalytics.exception.FileUploadException;
import de.adorsys.smartanalytics.exception.ResourceNotFoundException;
import de.adorsys.smartanalytics.pers.api.ContractBlacklistEntity;
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
@RequestMapping(path = "api/v1/config/contract-blacklist")
public class ContractBlacklistController {

    @Autowired
    private AnalyticsConfigProvider analyticsConfigProvider;
    @Autowired
    private ContractBlacklistService contractBlacklistService;

    @ApiOperation(
            value = "Read contract blacklist configuration",
            authorizations = {
                    @Authorization(value = "multibanking_auth", scopes = {
                            @AuthorizationScope(scope = "openid", description = "")
                    })})
    @GetMapping
    public Resource<ContractBlacklist> getContractBlackList() {
        ContractBlacklistEntity contractBlacklist = analyticsConfigProvider.getContractBlacklist();
        if (contractBlacklist == null) {
            throw new ResourceNotFoundException(ContractBlacklist.class, "contract-blacklist");
        }

        return new Resource(contractBlacklist);
    }

    @ApiOperation(
            value = "Update contract blacklist configuration",
            authorizations = {
                    @Authorization(value = "multibanking_auth", scopes = {
                            @AuthorizationScope(scope = "openid", description = "")
                    })})
    @PutMapping
    public HttpEntity<Void> updateContractBlackList(@RequestBody ContractBlacklist contractBlacklist) {
        contractBlacklistService.saveContractBlacklist(contractBlacklist);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(
            value = "Upload contract blacklist configuration file",
            authorizations = {
                    @Authorization(value = "multibanking_auth", scopes = {
                            @AuthorizationScope(scope = "openid", description = "")
                    })})
    @PostMapping(path = "/upload")
    public HttpEntity<?> uploadContractBlackList(@RequestParam MultipartFile contractBlacklistFile) {
        if (!contractBlacklistFile.isEmpty()) {
            try {
                contractBlacklistService.saveContractBlacklist(ImportUtils.importContractBlackList(contractBlacklistFile.getInputStream()));

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
