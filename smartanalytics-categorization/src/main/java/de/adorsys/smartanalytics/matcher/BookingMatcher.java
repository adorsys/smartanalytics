package de.adorsys.smartanalytics.matcher;

import de.adorsys.smartanalytics.api.Matcher;
import de.adorsys.smartanalytics.api.WrappedBooking;

public interface BookingMatcher extends Matcher {

    boolean isFinal();

    String getId();

    void extend(WrappedBooking wrappedBooking);
}
