package de.adorsys.smartanalytics.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.adorsys.smartanalytics.core.RulesProvider;
import de.adorsys.smartanalytics.core.RulesService;
import de.adorsys.smartanalytics.exception.InvalidRulesException;
import de.adorsys.smartanalytics.exception.ResourceNotFoundException;
import de.adorsys.smartanalytics.pers.api.RuleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by alexg on 07.02.17.
 */
@RestController
@SuppressWarnings("unused")
@RequestMapping(path = "api/v1/rules")
@Slf4j
public class RulesController {

    @Autowired
    private RulesProvider rulesProvider;
    @Autowired
    private RulesService rulesService;

    @RequestMapping(method = RequestMethod.GET)
    public PagedResources<Resource<RuleEntity>> getRules(@PageableDefault(size = 20) Pageable pageable,
                                                         PagedResourcesAssembler<RuleEntity> assembler) {
        Page<RuleEntity> pageableResult = rulesService.findAll(pageable);
        return assembler.toResource(pageableResult);
    }

    @RequestMapping(value = "/{ruleId}", method = RequestMethod.GET)
    public Resource<RuleEntity> getRule(@PathVariable String ruleId) {
        RuleEntity ruleEntity = rulesService.getRuleByRuleId(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException(RuleEntity.class, ruleId));

        return mapToResource(ruleEntity);
    }

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Void> createRule(@RequestBody RuleEntity ruleEntity) {
        ruleEntity.updateSearchIndex();
        rulesService.save(ruleEntity);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{ruleId}", method = RequestMethod.PUT)
    public HttpEntity<Void> updateRule(@PathVariable String ruleId, @RequestBody RuleEntity ruleEntity) {
        ruleEntity.updateSearchIndex();
        rulesService.save(ruleEntity);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{ruleId}", method = RequestMethod.DELETE)
    public HttpEntity<Void> deleteRule(@PathVariable String ruleId) {
        rulesService.deleteById(ruleId);
        log.info("Rule [{}] deleted.", ruleId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Resources<RuleEntity> searchRules(@RequestParam String query) {
        return new Resources<>(rulesService.search(query));
    }

    @RequestMapping(path = "/download", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public HttpEntity<InputStreamResource> downloadRules(@RequestParam(required = false, defaultValue = "CSV") RulesService.FileFormat format) throws IOException {
        List<RuleEntity> rules = rulesProvider.getRules();
        final YAMLFactory ymlFactory = new YAMLFactory();
        ObjectMapper objectMapper = new ObjectMapper(ymlFactory);

        return ResponseEntity.ok()
                .body(new InputStreamResource(new ByteArrayInputStream(rulesService.rulesAsByteArray(format))));
    }

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public HttpEntity<?> uploadRules(@RequestParam MultipartFile rulesFile) {
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

    private List<Resource> mapToResources(List<RuleEntity> entities) {
        return entities.stream()
                .map(this::mapToResource)
                .collect(toList());
    }

    private Resource<RuleEntity> mapToResource(RuleEntity entity) {
        return new Resource<>(entity,
                linkTo(methodOn(RulesController.class).getRule(entity.getId())).withSelfRel());
    }

}
