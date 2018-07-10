package de.adorsys.smartanalytics.api.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigStatus {

    private String rulesVersion;
    private String groupConfigVersion;
    private String categoriesVersion;
    private String contractBlackListVersion;
    private LocalDateTime lastChangeDate;

}
