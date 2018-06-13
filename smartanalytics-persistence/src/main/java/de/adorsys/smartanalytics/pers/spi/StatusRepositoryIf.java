package de.adorsys.smartanalytics.pers.spi;

import de.adorsys.smartanalytics.pers.api.StatusEntity;

import java.util.Optional;

public interface StatusRepositoryIf {

    Optional<StatusEntity> findById(String statusId);

    void save(StatusEntity statusEntity);
}