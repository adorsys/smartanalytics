package de.adorsys.smartanalytics.pers.spi;

import de.adorsys.smartanalytics.api.BookingGroupConfig;
import de.adorsys.smartanalytics.pers.api.BookingGroupConfigEntity;

import java.util.Optional;

public interface BookingGroupRepositoryIf {

    Optional<BookingGroupConfigEntity> getBookingGroups();

    void saveBookingGroups(BookingGroupConfig groupsContainer);

}
