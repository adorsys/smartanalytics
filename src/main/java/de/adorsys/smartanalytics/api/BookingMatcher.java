package de.adorsys.smartanalytics.api;

public interface BookingMatcher extends Matcher {

    void extend(WrappedBooking wrappedBooking);

    boolean isFinal();

    String getId();
}
