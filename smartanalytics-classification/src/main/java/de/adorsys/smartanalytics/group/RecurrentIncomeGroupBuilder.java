package de.adorsys.smartanalytics.group;

import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.config.Group;
import de.adorsys.smartanalytics.api.WrappedBooking;

import java.math.BigDecimal;

public class RecurrentIncomeGroupBuilder extends AbstractGroupBuilder {

    private static final Group.Type type = Group.Type.RECURRENT_INCOME;

    public RecurrentIncomeGroupBuilder(String groupName) {
        super(groupName);
    }

    @Override
    public boolean groupShouldBeCreated(WrappedBooking booking) {
        return isRecurrent(booking);
    }

    @Override
    public BookingGroup createGroup(WrappedBooking booking) {
        if (booking == null) {
            return null;
        }

        if (booking.getRuleIds() != null) {
            return new BookingGroup(booking.getBankConnection(), booking.getRuleIds().toString(), getGroupName(), getGroupType());
        }
        return new BookingGroup(booking.getBankConnection(), booking.getPurpose(), getGroupName(), getGroupType());
    }

    @Override
    public Group.Type getGroupType() {
        return type;
    }

    /**
     * Assert if the given booking is a recurrent income booking.
     *
     * @param booking the booking to be asserted
     * @return true if the booking is a recurrent income booking otherwise false.
     */
    public static boolean isRecurrent(WrappedBooking booking) {
        // should only have positive amount
        if (booking.getAmount() == null || booking.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }
        // should have a purpose
        if (booking.getPurpose() == null) {
            return false;
        }
        // should not have a valid bank connection
        return booking.getIban() != null
                || (booking.getBankCode() != null && booking.getAccountNumber() != null)
                || booking.getReferenceName() != null;
    }



}
