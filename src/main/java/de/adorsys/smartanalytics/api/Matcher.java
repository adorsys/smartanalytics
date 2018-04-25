package de.adorsys.smartanalytics.api;

public interface Matcher {
    /**
     * Assert the object matches this matcher.
     *
     * @param obj the object to match
     * @return true if the matcher matches otherwise false.
     */
    boolean match(WrappedBooking obj);
}
