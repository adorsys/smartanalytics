package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.matcher.BookingMatcher;
import de.adorsys.smartanalytics.pers.api.BookingGroupConfigEntity;
import de.adorsys.smartanalytics.pers.api.CategoriesTreeEntity;
import de.adorsys.smartanalytics.pers.api.ContractBlacklistEntity;
import de.adorsys.smartanalytics.pers.api.RuleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static de.adorsys.smartanalytics.utils.RulesFactory.createExpressionMatcher;
import static de.adorsys.smartanalytics.utils.RulesFactory.createSimilarityMatcher;

@Slf4j
@Service
public class AnalyticsConfigProvider {

    @Autowired
    private RulesService rulesService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private ContractBlacklistService contractBlacklistService;
    @Autowired
    private BookingGroupsService bookingGroupsService;
    @Autowired
    private CategoriesService categoriesService;

    private List<RuleEntity> rules;
    private List<BookingMatcher> incomingRules;
    private List<BookingMatcher> expensesRules;
    private CategoriesTreeEntity categoriesContainer;
    private BookingGroupConfigEntity bookingGroupConfig;
    private ContractBlacklistEntity contractBlacklist;

    @PostConstruct
    public void postConstruct() {
        initRules(rulesService.findAll());
        initCategories();
        initGroupConfig();
        initContractBlacklist();

        log.info("\n----------------------------------------------------------\n\t" +
                        "Smartanalytics config initialized\n\t" +
                        "Rules version: \t\t\t\t{}\n\t" +
                        "Categories version: \t\t{}\n\t" +
                        "Groups version: \t\t\t{}\n\t" +
                        "Contract blacklist version: \t{}\n----------------------------------------------------------",
                statusService.getStatus().getRulesVersion(),
                categoriesContainer != null ? categoriesContainer.getVersion(): "",
                bookingGroupConfig != null ? bookingGroupConfig.getVersion(): "",
                contractBlacklist != null ? contractBlacklist.getVersion(): "");
    }

    void initCategories() {
        categoriesService.getCategories()
                .ifPresent(categoriesContainerEntity -> this.categoriesContainer = categoriesContainerEntity);
    }

    void initGroupConfig() {
        bookingGroupsService.getBookingGroups()
                .ifPresent(bookingGroupConfigEntity -> this.bookingGroupConfig = bookingGroupConfigEntity);
    }

    void initContractBlacklist() {
        contractBlacklistService.getContractBlacklist()
                .ifPresent(contractBlacklistEntity -> this.contractBlacklist = contractBlacklistEntity);
    }

    void initRules(List<RuleEntity> rules) {
        this.rules = rules.stream()
                .sorted(Comparator.comparing(o -> new Integer(o.getOrder())))
                .collect(Collectors.toList());

        incomingRules = new ArrayList<>();
        expensesRules = new ArrayList<>();

        this.rules.forEach(rule -> {
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

    public CategoriesTreeEntity getCategoriesContainer() {
        return categoriesContainer;
    }

    public BookingGroupConfigEntity getBookingGroupConfig() {
        return bookingGroupConfig;
    }

    public ContractBlacklistEntity getContractBlacklist() {
        return contractBlacklist;
    }
}
