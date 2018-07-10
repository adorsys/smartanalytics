package de.adorsys.smartanalytics.contract;

import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.config.Group;
import de.adorsys.smartanalytics.api.Matcher;
import de.adorsys.smartanalytics.api.WrappedBooking;

import java.util.List;

public final class ContractValidator {

    public static boolean isContract(BookingGroup bookingGroup, List<WrappedBooking> bookings, List<Matcher> blackList) {
        return (bookingGroup.getCycle() != null && bookingGroup.getGroupType() != Group.Type.STANDING_ORDER)
                && !isBlackListed(bookings, blackList);
    }

    private static boolean isBlackListed(List<WrappedBooking> bookings, List<Matcher> blackList) {
        if (blackList == null) {
            return false;
        }

        for (Matcher matcher : blackList) {
                if (matcher.match(bookings.get(0))) {
                    return true;
                }
        }
        return false;
    }
}
