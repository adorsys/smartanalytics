package de.adorsys.smartanalytics.pers.file;

import com.fasterxml.jackson.core.type.TypeReference;
import de.adorsys.smartanalytics.pers.api.RuleEntity;
import de.adorsys.smartanalytics.pers.spi.RuleRepositoryIf;
import de.adorsys.smartanalytics.pers.utils.ImportUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Profile({"file-persistence"})
@Service
public class RuleRepositoryFileImpl implements RuleRepositoryIf {

    private static final String RULES_FILE_NAME = "rules.yml";

    @Value("${smartanalytics.conf.path:#{systemProperties['java.io.tmpdir']}}")
    private File confDir;

    private List<RuleEntity> rules = new ArrayList<>();

    @PostConstruct
    public void postConstruct() throws IOException {
        File rulesFile = new File(confDir, RULES_FILE_NAME);
        if (rulesFile.exists()) {
            rules.forEach(ruleEntity -> ruleEntity.setId(UUID.randomUUID().toString()));
            rules = ImportUtils.readAsYaml(rulesFile, new TypeReference<>() {
            });
        }
    }

    @Override
    public long count() {
        return rules.size();
    }

    @Override
    public void deleteAll() {
        rules = new ArrayList<>();

        File rulesFile = new File(confDir, RULES_FILE_NAME);
        if (rulesFile.exists()) {
            rulesFile.delete();
        }
    }

    @Override
    public void saveAll(List<RuleEntity> newRules) {
        newRules.forEach(ruleEntity -> ruleEntity.setId(UUID.randomUUID().toString()));
        rules = newRules;

        try {
            ImportUtils.writeObjectToYaml(new File(confDir, RULES_FILE_NAME), rules);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(RuleEntity ruleEntity) {
        if (ruleEntity.getId() == null) {
            ruleEntity.setId(UUID.randomUUID().toString());
        } else {
            rules.removeIf(existingRule -> existingRule.getId().equals(ruleEntity.getId()));
        }
        rules.add(ruleEntity);
        saveAll(rules);
    }

    @Override
    public List<RuleEntity> findAll() {
        return rules;
    }

    @Override
    public void deleteById(String id) {
        rules.removeIf(existingRule -> existingRule.getId().equals(id));
    }

    @Override
    public Page<RuleEntity> findAll(Pageable pageable) {
        if (pageable.isUnpaged()) {
            return new PageImpl<>(rules, Pageable.unpaged(), rules.size());
        }

        List<RuleEntity> pagedRules = rules.stream()
            .skip((long) pageable.getPageNumber() * pageable.getPageSize())
            .limit(pageable.getPageSize()).parallel().collect(Collectors.toList());

        return new PageImpl<>(pagedRules, pageable, rules.size());
    }

    @Override
    public Optional<RuleEntity> getRuleByRuleId(String ruleId) {
        return rules.stream().filter(ruleEntity -> ruleEntity.getRuleId().equals(ruleId)).findFirst();
    }

    @Override
    public Iterable<RuleEntity> search(String query) {
        return rules.stream().filter(ruleEntity -> ruleEntity.getRuleId().equals(query)).collect(Collectors.toList());
    }
}
