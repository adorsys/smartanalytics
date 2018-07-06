package de.adorsys.smartanalytics.pers.spi;

import de.adorsys.smartanalytics.pers.api.ConfigStatusEntity;

import java.util.Optional;

public interface StatusRepositoryIf {

    Optional<ConfigStatusEntity> findById(String statusId);

    void save(ConfigStatusEntity statusEntity);
}