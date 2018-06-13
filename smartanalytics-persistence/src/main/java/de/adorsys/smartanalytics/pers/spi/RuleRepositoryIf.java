package de.adorsys.smartanalytics.pers.spi;

import de.adorsys.smartanalytics.pers.api.RuleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by alexg on 07.02.17.
 */
public interface RuleRepositoryIf {

    long count();

    void deleteAll();

    void saveAll(List<RuleEntity> rules);

    void save(RuleEntity ruleEntity);

    List<RuleEntity> findAll();

    void deleteById(String id);

    Page<RuleEntity> findAll(Pageable pageable);

    Optional<RuleEntity> getRuleByRuleId(String ruleId);

    Iterable<RuleEntity> search(String query);
}
