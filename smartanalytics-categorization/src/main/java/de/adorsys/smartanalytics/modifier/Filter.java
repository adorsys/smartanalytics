package de.adorsys.smartanalytics.modifier;

import de.adorsys.smartanalytics.api.Booking;

public interface Filter {

    boolean filter(Booking booking);
}
