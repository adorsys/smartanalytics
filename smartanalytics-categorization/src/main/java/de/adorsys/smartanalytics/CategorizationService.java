package de.adorsys.smartanalytics;

import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.WrappedBooking;
import de.adorsys.smartanalytics.modifier.Modifier;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CategorizationService {

    public List<WrappedBooking> categorize(List<Booking> bookings, List<Modifier> modifierList) {
        log.info("categorize {} bookings", bookings.size());

        List<WrappedBooking> result = new ArrayList<>();
        for (Booking booking : bookings) {
            WrappedBooking wrappedBooking = new WrappedBooking(booking);
            for (Modifier modifier : modifierList) {
                modifier.modify(wrappedBooking);
            }
            result.add(wrappedBooking);
        }
        return result;
    }

}
