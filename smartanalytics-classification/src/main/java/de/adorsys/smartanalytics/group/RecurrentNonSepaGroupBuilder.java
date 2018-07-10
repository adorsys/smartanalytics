package de.adorsys.smartanalytics.group;

import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.config.Group;
import de.adorsys.smartanalytics.api.Matcher;
import de.adorsys.smartanalytics.api.WrappedBooking;

import java.math.BigDecimal;
import java.util.List;

public class RecurrentNonSepaGroupBuilder extends AbstractGroupBuilder {

    private static final Group.Type type = Group.Type.RECURRENT_NONSEPA;
    private List<Matcher> blacklist;

    /**
     * Sets the blacklist for recognizing a non sepa booking.
     * Each entry in the blacklist is a booking template.
     * Any non null attribute value have to match with a booking to be blocked.
     *
     * @param blacklist a list ob templates to match non sepa bookings.
     */
    public RecurrentNonSepaGroupBuilder(String groupName, List<Matcher> blacklist) {
        super(groupName);
        this.blacklist = blacklist;
    }

    @Override
    public boolean groupShouldBeCreated(WrappedBooking booking) {
        return isRecurrent(booking, blacklist);
    }

    @Override
    public BookingGroup createGroup(WrappedBooking booking) {
        if (booking == null) {
            return null;
        }
        if (booking.getRuleIds() != null) {
            return new BookingGroup(booking.getBankConnection(), booking.getRuleIds().toString(), getGroupName(), getGroupType());
        } else if (booking.getAmount() != null) {
            return new BookingGroup(booking.getBankConnection(), booking.getAmount().toPlainString(), getGroupName(), getGroupType());
        }
        return null;
    }

    /**
     * Assert if the given booking is a recurrent non sepa booking.
     * <p>
     * If a blacklist is defined each entry will be matched with the given input.
     * If all non null attribute values of the blacklist entry matches the booking will be refused.
     *
     * @param booking   the booking to be asserted
     * @param blacklist booking templates to be false
     * @return true if the booking is a recurrent non sepa booking otherwise false.
     */
    public static boolean isRecurrent(WrappedBooking booking, List<Matcher> blacklist) {
        // should only have negative amount
        if (booking.getAmount() == null || booking.getAmount().compareTo(BigDecimal.ZERO) >= 0) {
            return false;
        }
        // should have a purpose
        if (booking.getPurpose() == null) {
            return false;
        }
        // should not have a valid creditorId
        if (booking.getCreditorId() != null) {
            return false;
        }

        for (Matcher backlistEntry : blacklist) {
            if (backlistEntry.match(booking)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Group.Type getGroupType() {
        return type;
    }
}
