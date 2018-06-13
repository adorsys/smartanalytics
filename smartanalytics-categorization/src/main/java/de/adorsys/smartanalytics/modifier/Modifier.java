package de.adorsys.smartanalytics.modifier;

import de.adorsys.smartanalytics.api.WrappedBooking;

public interface Modifier {

    void modify(WrappedBooking booking);
}
