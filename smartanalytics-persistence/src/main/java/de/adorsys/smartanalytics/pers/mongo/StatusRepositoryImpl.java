package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.pers.api.ConfigStatusEntity;
import de.adorsys.smartanalytics.pers.spi.StatusRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Profile({"mongo-persistence", "fongo"})
@Service
public class StatusRepositoryImpl implements StatusRepositoryIf {

    @Autowired
    private StatusRepositoryMongodb statusRepository;

    @Override
    public Optional<ConfigStatusEntity> findById(String statusId) {
        return statusRepository.findById(statusId);
    }

    @Override
    public void save(ConfigStatusEntity statusEntity) {
        statusRepository.save(statusEntity);
    }
}
