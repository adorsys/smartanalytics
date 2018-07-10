package de.adorsys.smartanalytics.pers.spi;

import de.adorsys.smartanalytics.api.config.GroupConfig;
import de.adorsys.smartanalytics.pers.api.BookingGroupConfigEntity;

import java.util.Optional;

public interface BookingGroupRepositoryIf {

    Optional<BookingGroupConfigEntity> getBookingGroups();

    void saveBookingGroups(GroupConfig groupsContainer);

}
