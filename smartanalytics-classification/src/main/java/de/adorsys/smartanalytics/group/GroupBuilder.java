package de.adorsys.smartanalytics.group;

import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.config.Group;
import de.adorsys.smartanalytics.api.WrappedBooking;

/**
 * A cetegorizer will group bookings.
 * It will only be called once for each booking.
 * If a categorizer matches no other categorizer will be called.
 *
 */
public interface GroupBuilder {

    Group.Type getGroupType();

    /**
     * Asserts wether a categorizer should be created for the given booking.
     *
     * @param booking the booking to be grouped
     * @return true is categorizer should be created otherwise false
     */
    boolean groupShouldBeCreated(WrappedBooking booking);

    /**
     * Creates possible categories for this booking.
     * The categories should not contain any bookings and not equals to each other.
     * <p>
     * The array defines the priority of the categories.
     * If the first entry of the array will result an valid category the others will be invalid for this booking.
     *
     * @param booking the booking to be grouped
     * @return a list of categories for this booking
     */
    BookingGroup createGroup(WrappedBooking booking);
}
