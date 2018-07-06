package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.pers.api.BookingGroupConfigEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"mongo-persistence", "fongo"})
public interface BookingGroupsRepositoryMongodb extends MongoRepository<BookingGroupConfigEntity, String> {
}
