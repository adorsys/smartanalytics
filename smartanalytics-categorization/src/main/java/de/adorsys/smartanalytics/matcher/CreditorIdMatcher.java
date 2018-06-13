package de.adorsys.smartanalytics.matcher;

import de.adorsys.smartanalytics.api.Matcher;
import de.adorsys.smartanalytics.api.WrappedBooking;
import org.apache.commons.lang3.StringUtils;

public class CreditorIdMatcher implements Matcher {

    private String creditorId;

    public CreditorIdMatcher(String creditorId) {
        this.creditorId = creditorId;
    }

    @Override
    public boolean match(WrappedBooking input) {
        return StringUtils.equalsIgnoreCase(input.getCreditorId(), creditorId);
    }

    @Override
    public String toString() {
        return String.format("CREDITORID LIKE '%s'", creditorId);
    }
}
