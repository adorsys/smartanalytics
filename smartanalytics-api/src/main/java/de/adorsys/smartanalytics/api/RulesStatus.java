package de.adorsys.smartanalytics.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RulesStatus {

    private String version;
    private LocalDateTime lastChangeDate;
    private long countRules;

}
