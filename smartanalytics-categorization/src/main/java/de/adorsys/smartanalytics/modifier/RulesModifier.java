package de.adorsys.smartanalytics.modifier;


import de.adorsys.smartanalytics.api.WrappedBooking;
import de.adorsys.smartanalytics.matcher.BookingMatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * A rule modifier uses a rule set to modify a given booking.
 */
@Slf4j
public class RulesModifier implements Modifier {

    private List<? extends BookingMatcher> matchers;
    private Filter filter;

    public RulesModifier(List<? extends BookingMatcher> matchers, Filter filter) {
        this(matchers);
        this.filter = filter;
    }

    public RulesModifier(List<? extends BookingMatcher> matchers) {
        this.matchers = matchers;
    }

    @Override
    public void modify(WrappedBooking booking) {
        if (this.filter == null || this.filter.filter(booking.getBooking())) {
            useRulesOn(booking);
        }
    }

    /**
     * Use the rules from the rule set on the given wrappedBooking.
     * If a rule matches it will extend the wrappedBooking.
     * If no rule matches there will be created an extended wrappedBooking anyway.
     *
     * @param wrappedBooking the wrappedBooking to be expended
     * @return true if a matcher was found and ther rule was final
     */
    public boolean useRulesOn(WrappedBooking wrappedBooking) {
        for (BookingMatcher matcher : matchers) {
            try {
                if (matcher == null || !matcher.match(wrappedBooking)) {
                    continue;
                }
                log.debug("matcher [{}] matches booking [{}]", matcher.toString(), wrappedBooking);

                matcher.extend(wrappedBooking);
                if (matcher.isFinal()) {
                    return true;
                }
            } catch (Exception e) {
                log.error("invalid matcher {}", matcher.getId());
            }
        }
        return false;
    }
}
