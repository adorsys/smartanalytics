package de.adorsys.smartanalytics.pers.file;

import de.adorsys.smartanalytics.pers.api.ConfigStatusEntity;
import de.adorsys.smartanalytics.pers.spi.StatusRepositoryIf;
import de.adorsys.smartanalytics.pers.utils.ImportUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Profile({"file-persistence"})
@Service
public class StatusRepositoryFileImpl implements StatusRepositoryIf {

    private static final String STATUS_YML = "status.yml";

    @Value("${smartanalytics.conf.path:#{systemProperties['java.io.tmpdir']}}")
    private File confDir;

    private ConfigStatusEntity configStatusEntity;

    @PostConstruct
    public void postConstruct() throws IOException {
        File categoriesFile = new File(confDir, STATUS_YML);
        if (categoriesFile.exists()) {
            configStatusEntity = ImportUtils.readAsYaml(categoriesFile, ConfigStatusEntity.class);
        }
    }

    @Override
    public Optional<ConfigStatusEntity> findById(String statusId) {
        return Optional.ofNullable(configStatusEntity);
    }

    @Override
    public void save(ConfigStatusEntity statusEntity) {
        configStatusEntity = statusEntity;
        try {
            ImportUtils.writeObjectToYaml(new File(confDir, STATUS_YML), statusEntity);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
