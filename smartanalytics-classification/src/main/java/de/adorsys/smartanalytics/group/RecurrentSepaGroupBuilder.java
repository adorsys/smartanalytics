package de.adorsys.smartanalytics.group;

import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.config.Group;
import de.adorsys.smartanalytics.api.Matcher;
import de.adorsys.smartanalytics.api.WrappedBooking;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

/**
 * A categorizer for recurrent sepa bookings.
 * It will create the group with creditorid|mandatereference and applied rules:
 */
@Slf4j
public class RecurrentSepaGroupBuilder extends AbstractGroupBuilder {

    private static final Group.Type type = Group.Type.RECURRENT_SEPA;

    private List<Matcher> blacklist;

    public RecurrentSepaGroupBuilder(String groupName, List<Matcher> blacklist) {
        super(groupName);
        this.blacklist = blacklist;
    }

    @Override
    public boolean groupShouldBeCreated(WrappedBooking booking) {
        return isSepaBooking(booking, blacklist);
    }

    @Override
    public BookingGroup createGroup(WrappedBooking booking) {
        if (booking == null || booking.getPurpose() == null || booking.getMandateReference() == null || booking.getCreditorId() == null ||  booking.getRuleIds() == null) {
            return null;
        }

        log.trace("create group for {}, {}", booking.getCreditorId(), booking.getMandateReference());
        return new BookingGroup(booking.getCreditorId()+"|"+booking.getMandateReference(), booking.getRuleIds().toString(), getGroupName(), getGroupType());
    }

    /**
     * Assert if the given booking is a sepa booking.
     * <p>
     * If a blacklist is defined each entry will be matched with the given input.
     * If all non null attribute values of the blacklist entry matches the booking will be refused.
     *
     * @param booking   the booking to be asserted
     * @param blacklist booking templates to be false
     * @return true if the booking is a sepa booking otherwise false.
     */
    public static boolean isSepaBooking(WrappedBooking booking, List<Matcher> blacklist) {
        // should only have negative amount
        if (booking.getAmount() == null || booking.getAmount().compareTo(new BigDecimal("0.00")) >= 0) {
            return false;
        }
        // should have a purpose
        if (booking.getPurpose() == null) {
            return false;
        }
        // should have a valid creditorId
        if (booking.getCreditorId() == null) {
            return false;
        }
        // should be categorized
        if (booking.getRuleIds() == null) {
            return false;
        }
        // should not be on blacklist
        if (blacklist != null) {
            for (Matcher backlistEntry : blacklist) {
                if (backlistEntry.match(booking)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Group.Type getGroupType() {
        return type;
    }
}
