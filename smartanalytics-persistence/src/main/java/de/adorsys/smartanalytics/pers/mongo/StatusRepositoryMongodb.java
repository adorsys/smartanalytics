package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.pers.api.StatusEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Profile({"mongo-persistence", "fongo"})
@Repository("statusRepository")
public interface StatusRepositoryMongodb extends MongoRepository<StatusEntity, String> {

}