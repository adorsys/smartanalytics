package de.adorsys.smartanalytics.pers.file;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.adorsys.smartanalytics.pers.api.StatusEntity;
import de.adorsys.smartanalytics.pers.spi.StatusRepositoryIf;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Profile({"fs-persistence"})
@Service
public class StatusRepositoryImpl implements StatusRepositoryIf {

    @Value("file:/tmp/status.yml")
    private Resource statusResource;

    private ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    private StatusEntity statusEntity;

    @PostConstruct
    public void postConstruct() throws IOException {
        objectMapper.findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (!statusResource.getFile().exists()) {
            log.warn("status file {} not exists", statusResource.getFile().getAbsolutePath());
            return;
        }

        String rulesString = IOUtils.toString(statusResource.getInputStream(), StandardCharsets.UTF_8);
        this.statusEntity = objectMapper.readValue(rulesString, StatusEntity.class);
    }

    @Override
    public Optional<StatusEntity> findById(String statusId) {
        return Optional.ofNullable(statusEntity);
    }

    @Override
    public void save(StatusEntity statusEntity) {
        this.statusEntity = statusEntity;
        dumpToFile();
    }

    private void dumpToFile() {
        try {
            objectMapper.writeValue(statusResource.getFile(), statusEntity);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
