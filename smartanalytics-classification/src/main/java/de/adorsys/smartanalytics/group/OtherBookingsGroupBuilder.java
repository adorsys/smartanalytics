package de.adorsys.smartanalytics.group;

import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.config.Group;
import de.adorsys.smartanalytics.api.WrappedBooking;

public class OtherBookingsGroupBuilder extends AbstractGroupBuilder {

    private boolean income;

    public OtherBookingsGroupBuilder(String name, boolean income) {
        super(name);
        this.income = income;
    }

    @Override
    public boolean groupShouldBeCreated(WrappedBooking booking) {
        return true;
    }

    @Override
    public BookingGroup createGroup(WrappedBooking booking) {
        return new BookingGroup(getGroupName(), null, getGroupName(), getGroupType());
    }

    @Override
    public Group.Type getGroupType() {
        return income ? Group.Type.OTHER_INCOME : Group.Type.OTHER_EXPENSES;
    }
}
