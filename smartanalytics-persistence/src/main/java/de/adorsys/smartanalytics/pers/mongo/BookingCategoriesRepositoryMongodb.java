package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.pers.api.CategoriesTreeEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile({"mongo-persistence", "fongo"})
public interface BookingCategoriesRepositoryMongodb extends MongoRepository<CategoriesTreeEntity, String> {
}
