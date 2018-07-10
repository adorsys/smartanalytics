package de.adorsys.smartanalytics.group;

import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.config.Group;
import de.adorsys.smartanalytics.api.Matcher;
import de.adorsys.smartanalytics.api.WrappedBooking;

import java.util.List;

public class CustomGroupBuilder extends AbstractGroupBuilder {

    private static final Group.Type type = Group.Type.CUSTOM;
    private final List<Matcher> matchers;

    public CustomGroupBuilder(String groupName, List<Matcher> matchers) {
        super(groupName);
        this.matchers = matchers;
    }

    @Override
    public boolean groupShouldBeCreated(WrappedBooking booking) {
        if (booking == null || matchers == null) {
            return false;
        }
        for (Matcher matcher : matchers) {
            if (matcher.match(booking)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BookingGroup createGroup(WrappedBooking booking) {
        return new BookingGroup(getGroupName(), null, getGroupName(), getGroupType());
    }

    @Override
    public Group.Type getGroupType() {
        return type;
    }
}
