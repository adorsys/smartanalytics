package de.adorsys.smartanalytics.pers.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.adorsys.smartanalytics.pers.api.RuleEntity;
import de.adorsys.smartanalytics.pers.spi.RuleRepositoryIf;
import de.adorsys.smartanalytics.pers.utils.ImportUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Profile({"fs-persistence"})
@Repository
public class RuleRepositoryImpl implements RuleRepositoryIf {

    @Value("file:/tmp/rules.yml")
    private Resource rulesResource;

    private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    private List<RuleEntity> rules;

    @PostConstruct
    public void postConstruct() throws IOException {
        if (!rulesResource.getFile().exists()) {
            this.rules = new ArrayList<>();
            log.warn("rules file {} not exists", rulesResource.getFile().getAbsolutePath());
            return;
        }
        this.rules = ImportUtils.importYamlRules(IOUtils.toString(rulesResource.getInputStream(), StandardCharsets.UTF_8));
    }

    @Override
    public long count() {
        return rules.size();
    }

    @Override
    public void deleteAll() {
        rules = new ArrayList<>();
        dumpToFile();
    }

    @Override
    public void saveAll(List<RuleEntity> rules) {
        this.rules = rules;
        dumpToFile();
    }

    @Override
    public void save(RuleEntity ruleEntity) {
        if (ruleEntity.getId() == null) {
            ruleEntity.setId(UUID.randomUUID().toString());
            rules.add(ruleEntity);
        } else {
            rules.removeIf(x -> x.getId().equals(ruleEntity.getId()));
            rules.add(ruleEntity);
        }
        dumpToFile();
    }

    @Override
    public List<RuleEntity> findAll() {
        return rules;
    }

    @Override
    public void deleteById(String id) {
        rules.removeIf(x -> x.getId().equals(id));
        dumpToFile();
    }

    @Override
    public Page<RuleEntity> findAll(Pageable pageable) {
        return createPage(pageable, rules);
    }

    @Override
    public Optional<RuleEntity> getRuleByRuleId(String ruleId) {
        return rules.stream().filter(ruleEntity -> ruleEntity.getRuleId().equals(ruleId)).findFirst();
    }

    @Override
    public Iterable<RuleEntity> search(String query) {
        Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        return rules.stream().filter(ruleEntity -> ruleEntity.getSearchIndex()
                .stream()
                .anyMatch(s -> pattern.matcher(s).find()))
                .collect(Collectors.toList());
    }

    private PageImpl<RuleEntity> createPage(Pageable pageable, List<RuleEntity> rules) {
        List<RuleEntity> result = subList(rules.iterator(), pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(result, pageable, rules.size());
    }

    private List<RuleEntity> subList(Iterator<RuleEntity> source, long skip, int limit) {
        final List<RuleEntity> result = new ArrayList<>();
        while (source.hasNext()) {
            RuleEntity t = source.next();
            if (skip > 0) {
                skip--;
            } else {
                result.add(t);
                limit--;
            }
            if (limit + skip == 0) break;
        }
        return result;
    }

    private void dumpToFile() {
        try {
            objectMapper.writeValue(rulesResource.getFile(), rules);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
