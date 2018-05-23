package de.adorsys.smartanalytics.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupConfig {

    private List<Group> groups;
    private List<String> recurrentWhiteListMatcher;

}
