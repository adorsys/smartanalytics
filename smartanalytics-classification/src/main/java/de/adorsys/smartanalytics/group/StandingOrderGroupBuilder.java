package de.adorsys.smartanalytics.group;

import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.config.Group;
import de.adorsys.smartanalytics.api.WrappedBooking;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StandingOrderGroupBuilder extends AbstractGroupBuilder {

    private static final Group.Type type = Group.Type.STANDING_ORDER;

    public StandingOrderGroupBuilder(String groupName) {
        super(groupName);
    }

    @Override
    public boolean groupShouldBeCreated(WrappedBooking booking) {
        if (booking == null) {
            return false;
        }
        return booking.isStandingOrder();
    }

    @Override
    public BookingGroup createGroup(WrappedBooking booking) {
        BookingGroup bookingGroup;
        if (booking.getCreditorId() != null && booking.getMandateReference() != null) {
            log.trace("interpreted as standing order booking with kref & mref");
            bookingGroup = new BookingGroup(booking.getCreditorId(), booking.getMandateReference(), getGroupName(), getGroupType());
        } else if (booking.getIban() != null) {
            log.trace("interpreted as standing order booking with iban");
            bookingGroup = new BookingGroup(booking.getIban(), booking.getAmount().toPlainString(), getGroupName(), getGroupType());
        } else {
            log.trace("interpreted as non sepa booking with amount");
            bookingGroup = new BookingGroup(booking.getReferenceName(), booking.getAmount().toPlainString(), getGroupName(), getGroupType());
        }

        return bookingGroup;
    }

    @Override
    public Group.Type getGroupType() {
        return type;
    }
}
