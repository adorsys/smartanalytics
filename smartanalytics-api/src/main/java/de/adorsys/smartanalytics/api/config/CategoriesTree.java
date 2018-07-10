package de.adorsys.smartanalytics.api.config;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CategoriesTree {

    private String version;
    private LocalDate changeDate;
    private List<Category> categories;
}
