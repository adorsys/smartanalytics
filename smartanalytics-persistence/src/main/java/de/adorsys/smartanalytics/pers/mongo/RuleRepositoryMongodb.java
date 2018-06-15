package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.pers.api.RuleEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by alexg on 07.02.17.
 */
@Profile({"mongo-persistence", "fongo"})
@Repository("ruleRepositoryMongo")
public interface RuleRepositoryMongodb extends MongoRepository<RuleEntity, String> {

    Optional<RuleEntity> getRuleByRuleId(String ruleId);
}
