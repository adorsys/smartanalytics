package de.adorsys.smartanalytics.api.config;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ContractBlacklist {

    private String version;
    private LocalDate changeDate;
    private List<String> expressions;
}
