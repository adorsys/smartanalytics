package de.adorsys.smartanalytics.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.adorsys.smartanalytics.api.Rule;
import de.adorsys.smartanalytics.pers.api.RuleEntity;
import de.adorsys.smartanalytics.pers.spi.RuleRepositoryIf;
import de.adorsys.smartanalytics.pers.utils.ImportUtils;
import de.adorsys.smartanalytics.pers.utils.RuleMixIn;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RulesService {

    @Autowired
    private RuleRepositoryIf rulesRepository;
    @Autowired
    private RuleRepositoryIf rulesRepositoryCustom;
    @Autowired
    private AnalyticsConfigProvider analyticsConfigProvider;
    @Autowired
    private StatusService statusService;

    public enum FileFormat {
        CSV, YAML
    }

    public void newRules(String fileName, InputStream inputStream) throws IOException {
        List<RuleEntity> rules = ImportUtils.importRules(inputStream);
        rules.forEach(RuleEntity::updateSearchIndex);
        log.info("new [{}] rules", rules.size());

        rulesRepository.deleteAll();
        rulesRepository.saveAll(rules);
        analyticsConfigProvider.initRules(rules);
        statusService.rulesChanged(FilenameUtils.getBaseName(fileName));
    }

    public byte[] rulesAsByteArray(FileFormat format) throws IOException {
        List<RuleEntity> rules = analyticsConfigProvider.getRules();

        if (format == FileFormat.YAML) {
            final YAMLFactory ymlFactory = new YAMLFactory();
            ObjectMapper objectMapper = new ObjectMapper(ymlFactory);
            return objectMapper.writeValueAsBytes(rules);
        } else {
            return rulesToCsv(rules, Rule.class, RuleMixIn.class);
        }
    }

    private byte[] rulesToCsv(List<?> list, Class<?> type, Class<?> mixin) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = csvMapper.schemaFor(type).withHeader().withColumnSeparator(';');

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (SequenceWriter csvWriter = csvMapper
                .addMixIn(type, mixin)
                .writerWithDefaultPrettyPrinter()
                .with(csvSchema)
                .forType(type)
                .writeValues(outputStream)) {
            for (Object nextRow : list) {
                csvWriter.write(nextRow);
            }
        }

        return outputStream.toByteArray();
    }

    public List<RuleEntity> findAll() {
        return rulesRepository.findAll();
    }

    public Page<RuleEntity> findAll(Pageable pageable) {
        return rulesRepository.findAll(pageable);
    }

    public Optional<RuleEntity> getRuleByRuleId(String ruleId) {
        return rulesRepository.getRuleByRuleId(ruleId);
    }

    public void save(RuleEntity ruleEntity) {
        List<RuleEntity> existingRules = analyticsConfigProvider.getRules();

        //find rule with same order -> reorder needed
        Optional<RuleEntity> indexChangedRule = existingRules.stream()
                .filter(existingRule -> existingRule.getOrder() == ruleEntity.getOrder())
                .filter(existingRule -> !StringUtils.equals(existingRule.getId(), ruleEntity.getId()))
                .findFirst();

        if (indexChangedRule.isPresent()) {
            int newIndex = existingRules.indexOf(indexChangedRule.get());
            existingRules.removeIf(existingRule -> StringUtils.equals(existingRule.getId(), ruleEntity.getId()));
            existingRules.add(newIndex, ruleEntity);

            for (int i = 0; i < existingRules.size(); i++) {
                existingRules.get(i).setOrder(i+1);
            }
            rulesRepository.saveAll(existingRules);
        } else {
            rulesRepository.save(ruleEntity);
        }

        analyticsConfigProvider.initRules(rulesRepository.findAll());
        statusService.rulesChanged();
    }

    public void deleteById(String id) {
        rulesRepository.deleteById(id);
        analyticsConfigProvider.initRules(rulesRepository.findAll());
        statusService.rulesChanged();
    }

    public Iterable<RuleEntity> search(String query) {
        return rulesRepositoryCustom.search(query);
    }
}
