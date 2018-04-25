package de.adorsys.smartanalytics.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsRequest {

    private LocalDate referenceDate;
    private List<Booking> bookings;
    private List<Rule> customRules;
    private List<Group> groups;
    private List<String> groupWhiteListMatcher;
    private List<String> contractBlackListMatcher;

}
