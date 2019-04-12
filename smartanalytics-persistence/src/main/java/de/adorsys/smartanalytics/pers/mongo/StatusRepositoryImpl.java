package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.pers.api.ConfigStatusEntity;
import de.adorsys.smartanalytics.pers.spi.StatusRepositoryIf;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Profile({"mongo-persistence", "fongo"})
@Service
public class StatusRepositoryImpl implements StatusRepositoryIf {

    private final StatusRepositoryMongodb statusRepository;

    @Override
    public Optional<ConfigStatusEntity> findById(String statusId) {
        return statusRepository.findById(statusId);
    }

    @Override
    public void save(ConfigStatusEntity statusEntity) {
        statusRepository.save(statusEntity);
    }
}
