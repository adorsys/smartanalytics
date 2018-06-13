package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.CategorizationService;
import de.adorsys.smartanalytics.ClassificationService;
import de.adorsys.smartanalytics.api.*;
import de.adorsys.smartanalytics.group.*;
import de.adorsys.smartanalytics.matcher.BookingMatcher;
import de.adorsys.smartanalytics.modifier.Modifier;
import de.adorsys.smartanalytics.modifier.PaypalReceiverModifier;
import de.adorsys.smartanalytics.modifier.RulesModifier;
import de.adorsys.smartanalytics.utils.RulesFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static de.adorsys.smartanalytics.utils.RulesFactory.createExpressionMatcher;
import static de.adorsys.smartanalytics.utils.RulesFactory.createSimilarityMatcher;

@Slf4j
@Service
public class AnalyticsService {

    @Autowired
    private RulesProvider rulesProvider;
    @Autowired
    private StatusService statusService;

    public AnalyticsResult analytics(AnalyticsRequest request) {
        List<WrappedBooking> categorizedBookings = categorize(request.getBookings(), request.getCustomRules());
        List<BookingGroup> groups = groupBookings(request, categorizedBookings);

        AnalyticsResult analyticsResult = new AnalyticsResult();
        analyticsResult.setBookings(categorizedBookings);
        analyticsResult.setBookingGroups(groups);
        analyticsResult.setRulesStatus(statusService.getStatus());

        return analyticsResult;
    }

    private List<BookingGroup> groupBookings(AnalyticsRequest request, List<WrappedBooking> categorizedBookings) {
        List<GroupBuilder> builderList = getGroupBuilders(request.getGroupConfig());

        List<Matcher> groupWhiteListMatcher = request.getGroupConfig().getRecurrentWhiteListMatcher().stream()
                .map(RulesFactory::createExpressionMatcher)
                .collect(Collectors.toList());

        List<Matcher> contractBlackListMatcher = request.getContractBlackListMatcher().stream()
                .map(RulesFactory::createExpressionMatcher)
                .collect(Collectors.toList());

        return new ClassificationService().group(categorizedBookings, builderList, groupWhiteListMatcher, contractBlackListMatcher);
    }

    private List<GroupBuilder> getGroupBuilders(GroupConfig groupConfig) {
        return groupConfig.getGroups().stream().map(group -> {
            List<Matcher> matchers;

            switch (group.getType()) {
                case RECURRENT_SEPA:
                    matchers = group.getBlacklistMatcher().stream()
                            .map(RulesFactory::createExpressionMatcher)
                            .collect(Collectors.toList());
                    return new RecurrentSepaGroupBuilder(group.getName(), matchers);
                case STANDING_ORDER:
                    return new StandingOrderGroupBuilder(group.getName());
                case RECURRENT_INCOME:
                    return new RecurrentIncomeGroupBuilder(group.getName());
                case RECURRENT_NONSEPA:
                    matchers = group.getBlacklistMatcher().stream()
                            .map(RulesFactory::createExpressionMatcher)
                            .collect(Collectors.toList());
                    return new RecurrentNonSepaGroupBuilder(group.getName(), matchers);
                case CUSTOM:
                    matchers = group.getWhitelistMatcher().stream()
                            .map(RulesFactory::createExpressionMatcher)
                            .collect(Collectors.toList());
                    return new CustomGroupBuilder(group.getName(), matchers);
                case OTHER_INCOME:
                    return new OtherBookingsGroupBuilder(group.getName(), true);
                case OTHER_EXPENSES:
                    return new OtherBookingsGroupBuilder(group.getName(), false);
            }
            throw new IllegalArgumentException("invalid group type: " + group.getType());
        }).collect(Collectors.toList());
    }

    private List<WrappedBooking> categorize(List<Booking> bookings, List<Rule> customRules) {
        List<BookingMatcher> incomingRules = new ArrayList<>();
        List<BookingMatcher> expensesRules = new ArrayList<>();
        customRules.forEach(customRule -> {
            try {
                BookingMatcher matcher = customRule.getSimilarityMatchType() == null ? createExpressionMatcher(customRule) : createSimilarityMatcher(customRule);
                if (customRule.isIncoming()) {
                    incomingRules.add(matcher);
                } else {
                    expensesRules.add(matcher);
                }
            } catch (Exception e) {
                log.warn("invalid customRule [{}]", customRule.getRuleId());
            }
        });

        incomingRules.addAll(rulesProvider.getIncomingRules());
        expensesRules.addAll(rulesProvider.getExpensesRules());

        List<Modifier> modifier = new ArrayList<>();
        modifier.add(new RulesModifier(expensesRules, booking ->
                booking.getAmount() == null || booking.getAmount().compareTo(new BigDecimal("0.00")) < 0
        ));
        modifier.add(new RulesModifier(incomingRules, booking ->
                booking.getAmount() != null && booking.getAmount().compareTo(new BigDecimal("0.00")) >= 0
        ));

        modifier.add(new PaypalReceiverModifier());

        return new CategorizationService().categorize(bookings, modifier);
    }

}