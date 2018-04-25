package de.adorsys.smartanalytics.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class AnalyticsResult {

    private List<WrappedBooking> bookings = new ArrayList<>();
    private Set<BookingGroup> bookingGroups;
    private Budget budget;

}
