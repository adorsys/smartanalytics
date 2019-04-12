package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.api.config.GroupConfig;
import de.adorsys.smartanalytics.pers.spi.BookingGroupRepositoryIf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookingGroupsService {

    private final AnalyticsConfigProvider analyticsConfigProvider;
    private final BookingGroupRepositoryIf bookingGroupRepository;
    private final StatusService statusService;

    public void saveBookingGroups(GroupConfig groupsContainer) {
        bookingGroupRepository.saveBookingGroups(groupsContainer);
        statusService.groupConfigChanged(groupsContainer.getVersion());
        analyticsConfigProvider.initGroupConfig();
    }
}
