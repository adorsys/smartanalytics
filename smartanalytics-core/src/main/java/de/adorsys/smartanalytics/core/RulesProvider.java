package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.matcher.BookingMatcher;
import de.adorsys.smartanalytics.pers.api.RuleEntity;
import de.adorsys.smartanalytics.pers.spi.RuleRepositoryIf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static de.adorsys.smartanalytics.utils.RulesFactory.createExpressionMatcher;
import static de.adorsys.smartanalytics.utils.RulesFactory.createSimilarityMatcher;

@Slf4j
@Service
public class RulesProvider {

    @Autowired
    private RuleRepositoryIf ruleRepository;

    private List<RuleEntity> rules;
    private List<BookingMatcher> incomingRules;
    private List<BookingMatcher> expensesRules;

    @PostConstruct
    public void postConstruct() {
        initRules(ruleRepository.findAll());
    }

    void initRules(List<RuleEntity> rules) {
        incomingRules = new ArrayList<>();
        expensesRules = new ArrayList<>();

        rules.forEach(rule -> {
            try {
                BookingMatcher matcher = rule.getSimilarityMatchType() == null ? createExpressionMatcher(rule) : createSimilarityMatcher(rule);
                if (rule.isIncoming()) {
                    incomingRules.add(matcher);
                } else {
                    expensesRules.add(matcher);
                }
            } catch (Exception e) {
                log.warn("invalid rule [{}]", rule.getRuleId(), e);
            }
        });

        this.rules = rules;

        log.info("initialized [{}] rules", rules.size());

    }

    List<BookingMatcher> getIncomingRules() {
        return incomingRules;
    }

    List<BookingMatcher> getExpensesRules() {
        return expensesRules;
    }

    public List<RuleEntity> getRules() {
        return rules;
    }

}
