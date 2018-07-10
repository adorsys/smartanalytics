package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.api.config.GroupConfig;
import de.adorsys.smartanalytics.pers.api.BookingGroupConfigEntity;
import de.adorsys.smartanalytics.pers.spi.BookingGroupRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingGroupsService {

    @Autowired
    private AnalyticsConfigProvider analyticsConfigProvider;
    @Autowired
    private BookingGroupRepositoryIf bookingGroupRepository;
    @Autowired
    private StatusService statusService;

    public Optional<BookingGroupConfigEntity> getBookingGroups() {
        return bookingGroupRepository.getBookingGroups();
    }

    public void saveBookingGroups(GroupConfig groupsContainer) {
        bookingGroupRepository.saveBookingGroups(groupsContainer);
        statusService.groupConfigChanged(groupsContainer.getVersion());
        analyticsConfigProvider.initGroupConfig();
    }
}
