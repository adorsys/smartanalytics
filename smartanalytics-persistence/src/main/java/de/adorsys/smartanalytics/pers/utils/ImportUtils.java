package de.adorsys.smartanalytics.pers.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.adorsys.smartanalytics.api.config.CategoriesTree;
import de.adorsys.smartanalytics.api.config.ContractBlacklist;
import de.adorsys.smartanalytics.api.config.GroupConfig;
import de.adorsys.smartanalytics.pers.api.RuleEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class ImportUtils {

    private static ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static GroupConfig importBookingGroups(InputStream inputStream) throws IOException {
        return yamlObjectMapper.readValue(inputStream, GroupConfig.class);
    }

    public static ContractBlacklist importContractBlackList(InputStream inputStream) throws IOException {
        return yamlObjectMapper.readValue(inputStream, ContractBlacklist.class);
    }

    public static CategoriesTree importCategories(InputStream inputStream) throws IOException {
        return yamlObjectMapper.readValue(inputStream, CategoriesTree.class);
    }

    public static List<RuleEntity> importRules(InputStream inputStream) throws IOException {
        String rulesString = IOUtils.toString(new BOMInputStream(inputStream), StandardCharsets.UTF_8);
        try {
            return importCsvRules(RuleEntity.class, RuleMixIn.class, rulesString);
        } catch (Exception e) {
            log.debug("unable import rules as csv", e);
            return importYamlRules(rulesString);
        }
    }

    public static List<RuleEntity> importYamlRules(String rulesString) throws IOException {
        return yamlObjectMapper.readValue(rulesString, new TypeReference<List<RuleEntity>>() {
        });
    }

    private static <T> List<T> importCsvRules(Class<T> type, Class<?> mixin, String rulesString) throws Exception {
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');

        CsvMapper mapper = new CsvMapper();
        mapper.addMixIn(type, mixin);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        MappingIterator<T> readValues = mapper.readerFor(type).with(csvSchema).readValues(rulesString);
        return readValues.readAll();
    }


}
