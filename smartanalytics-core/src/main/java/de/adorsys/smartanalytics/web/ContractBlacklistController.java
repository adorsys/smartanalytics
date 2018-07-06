package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.api.ContractBlacklist;
import de.adorsys.smartanalytics.core.ContractBlacklistService;
import de.adorsys.smartanalytics.exception.InvalidCategoriesException;
import de.adorsys.smartanalytics.exception.ResourceNotFoundException;
import de.adorsys.smartanalytics.pers.api.ContractBlacklistEntity;
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
@RequestMapping(path = "api/v1/config/contract-blacklist")
public class ContractBlacklistController {

    @Autowired
    private ContractBlacklistService contractBlacklistService;

    @RequestMapping(method = RequestMethod.GET)
    public Resource<ContractBlacklist> getContractBlackList() {
        ContractBlacklistEntity contractBlacklist = contractBlacklistService.getContractBlacklist()
                .orElseThrow(() -> new ResourceNotFoundException(ContractBlacklist.class, "contract-blacklist"));

        return new Resource(contractBlacklist);
    }

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Void> updateContractBlackList(@RequestBody ContractBlacklist contractBlacklist) {
        contractBlacklistService.saveContractBlacklist(contractBlacklist);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public HttpEntity<?> uploadContractBlackList(@RequestParam MultipartFile bookingGroupsFile) {
        if (!bookingGroupsFile.isEmpty()) {
            try {
                contractBlacklistService.saveContractBlacklist(ImportUtils.importContractBlackList(bookingGroupsFile.getInputStream()));

                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("unable import groups", e);
                throw new InvalidCategoriesException(bookingGroupsFile.getOriginalFilename());
            }
        } else {
            throw new InvalidCategoriesException("File is empty");
        }
    }
}
