package de.adorsys.smartanalytics.pers.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.adorsys.smartanalytics.pers.api.RuleEntity;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@UtilityClass
public class ImportUtils {

    private static final ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static <T> T readAsYaml(InputStream inputStream, Class<T> responseClass) throws IOException {
        return yamlObjectMapper.readValue(inputStream, responseClass);
    }

    public static <T> T readAsYaml(File file, TypeReference<T> typeReference) throws IOException {
        return yamlObjectMapper.readValue(file, typeReference);
    }

    public static <T> T readAsYaml(File file, Class<T> responseClass) throws IOException {
        return yamlObjectMapper.readValue(file, responseClass);
    }

    public static <T> T readAsYaml(String rulesString, TypeReference<T> typeReference) throws IOException {
        return yamlObjectMapper.readValue(rulesString, typeReference);
    }

    public static List<RuleEntity> readRules(InputStream inputStream) throws IOException {
        String rulesString = IOUtils.toString(new BOMInputStream(inputStream), StandardCharsets.UTF_8);
        try {
            return readRulesAsCsv(RuleEntity.class, RuleMixIn.class, rulesString);
        } catch (Exception e) {
            log.debug("unable import rules as csv", e);
            return readAsYaml(rulesString, new TypeReference<>() {
            });
        }
    }

    public static void writeObjectToYaml(File dest, Object object) throws IOException {
        yamlObjectMapper.writeValue(dest, object);
    }

    private static <T> List<T> readRulesAsCsv(Class<T> type, Class<?> mixin, String rulesString) throws Exception {
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader().withColumnSeparator(';');

        CsvMapper mapper = new CsvMapper();
        mapper.addMixIn(type, mixin);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

        MappingIterator<T> readValues = mapper.readerFor(type).with(csvSchema).readValues(rulesString);
        return readValues.readAll();
    }


}
