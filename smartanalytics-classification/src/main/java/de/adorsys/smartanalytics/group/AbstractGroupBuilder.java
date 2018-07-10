package de.adorsys.smartanalytics.group;

import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.config.Group;
import de.adorsys.smartanalytics.api.WrappedBooking;
import lombok.Data;

@Data
public abstract class AbstractGroupBuilder implements GroupBuilder {

    private String groupName;

    public AbstractGroupBuilder(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public abstract boolean groupShouldBeCreated(WrappedBooking booking);

    @Override
    public abstract BookingGroup createGroup(WrappedBooking booking);

    public abstract Group.Type getGroupType();
}
