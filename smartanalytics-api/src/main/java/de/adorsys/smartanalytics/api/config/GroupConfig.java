package de.adorsys.smartanalytics.api.config;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GroupConfig {

    private String version;
    private LocalDate changeDate;
    private List<Group> bookingGroups;
    private List<String> recurrentWhiteListMatcher;
}
