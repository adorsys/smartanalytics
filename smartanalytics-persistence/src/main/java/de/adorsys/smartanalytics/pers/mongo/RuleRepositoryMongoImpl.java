package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.pers.api.RuleEntity;
import de.adorsys.smartanalytics.pers.spi.RuleRepositoryIf;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Profile({"mongo-persistence", "fongo"})
@Service
public class RuleRepositoryMongoImpl implements RuleRepositoryIf {

    private final RuleRepositoryMongodb ruleRepositoryMongo;
    private final MongoTemplate mongoTemplate;

    @Override
    public long count() {
        return ruleRepositoryMongo.count();
    }

    @Override
    public void deleteAll() {
        ruleRepositoryMongo.deleteAll();
    }

    @Override
    public void saveAll(List<RuleEntity> rules) {
        ruleRepositoryMongo.saveAll(rules);
    }

    @Override
    public void save(RuleEntity ruleEntity) {
        ruleRepositoryMongo.save(ruleEntity);
    }

    @Override
    public List<RuleEntity> findAll() {
        return ruleRepositoryMongo.findAll();
    }

    @Override
    public void deleteById(String id) {
        ruleRepositoryMongo.deleteById(id);
    }

    @Override
    public Page<RuleEntity> findAll(Pageable pageable) {
        return ruleRepositoryMongo.findAll(pageable);
    }

    @Override
    public Optional<RuleEntity> getRuleByRuleId(String ruleId) {
        return ruleRepositoryMongo.getRuleByRuleId(ruleId);
    }

    @Override
    public Iterable<RuleEntity> search(String search) {
        Collection<String> terms = new HashSet<>(Arrays.asList(search.split(" ")));

        Criteria[] criterias = terms
                .stream()
                .map(s -> Criteria.where("searchIndex").regex(s.toLowerCase(), "iu"))
                .toArray(Criteria[]::new);

        return mongoTemplate.find(Query.query(new Criteria().andOperator(criterias)).with(Sort.by("order")),
                RuleEntity.class);
    }
}
