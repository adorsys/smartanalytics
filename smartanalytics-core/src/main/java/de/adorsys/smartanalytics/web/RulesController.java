package de.adorsys.smartanalytics.web;

import de.adorsys.smartanalytics.core.RulesService;
import de.adorsys.smartanalytics.exception.InvalidRulesException;
import de.adorsys.smartanalytics.exception.ResourceNotFoundException;
import de.adorsys.smartanalytics.pers.api.RuleEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Tag(name = "Rules")
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "api/v1/config/booking-rules")
@Slf4j
public class RulesController {

    private final RulesService rulesService;

    @Operation(description = "Read categorization rules", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @GetMapping
    public PagedResources<Resource<RuleEntity>> getRules(@PageableDefault(size = 20, sort = "order") Pageable pageable,
                                                         PagedResourcesAssembler<RuleEntity> assembler) {
        Page<RuleEntity> pageableResult = rulesService.findAll(pageable);
        return assembler.toResource(pageableResult);
    }

    @Operation(description = "Read categorization rule", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @GetMapping(value = "/{ruleId}")
    public Resource<RuleEntity> getRule(@PathVariable String ruleId) {
        RuleEntity ruleEntity = rulesService.getRuleByRuleId(ruleId)
            .orElseThrow(() -> new ResourceNotFoundException(RuleEntity.class, ruleId));

        return mapToResource(ruleEntity);
    }

    @Operation(description = "Create categorization rule", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @PostMapping
    public HttpEntity<Void> createRule(@RequestBody RuleEntity ruleEntity) {
        ruleEntity.updateSearchIndex();
        rulesService.save(ruleEntity);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(description = "Update categorization rule", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @PutMapping(value = "/{ruleId}")
    public HttpEntity<Void> updateRule(@PathVariable String ruleId, @RequestBody RuleEntity ruleEntity) {
        ruleEntity.updateSearchIndex();
        rulesService.save(ruleEntity);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(description = "Delete categorization rule", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @DeleteMapping(value = "/{ruleId}")
    public HttpEntity<Void> deleteRule(@PathVariable String ruleId) {
        rulesService.deleteById(ruleId);
        log.info("Rule [{}] deleted.", ruleId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(description = "Find categorization rule", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @GetMapping(value = "/search")
    public Resources<RuleEntity> searchRules(@RequestParam String query) {
        return new Resources<>(rulesService.search(query));
    }

    @Operation(description = "Download categorization rules", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @GetMapping(path = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public HttpEntity<InputStreamResource> downloadRules(@RequestParam(required = false, defaultValue = "CSV") RulesService.FileFormat format) throws IOException {
        return ResponseEntity.ok()
            .body(new InputStreamResource(new ByteArrayInputStream(rulesService.rulesAsByteArray(format))));
    }

    @Operation(description = "Upload categorization rules file", security = {
        @SecurityRequirement(name = "multibanking_auth", scopes = "openid")})
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HttpEntity<Void> uploadRules(@RequestParam MultipartFile rulesFile) {
        if (!rulesFile.isEmpty()) {
            try {
                rulesService.newRules(rulesFile.getOriginalFilename(), rulesFile.getInputStream());

                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception e) {
                log.error("unable import rules", e);
                throw new InvalidRulesException(rulesFile.getOriginalFilename());
            }
        } else {
            throw new InvalidRulesException("File is empty");
        }
    }

    private Resource<RuleEntity> mapToResource(RuleEntity entity) {
        return new Resource<>(entity,
            linkTo(methodOn(RulesController.class).getRule(entity.getId())).withSelfRel());
    }

}
