package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.pers.api.StatusEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

@Profile({"mongo-persistence", "fongo"})
public interface StatusRepositoryMongodb extends MongoRepository<StatusEntity, String> {

}