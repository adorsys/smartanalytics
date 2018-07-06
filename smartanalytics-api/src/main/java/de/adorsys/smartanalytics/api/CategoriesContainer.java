package de.adorsys.smartanalytics.api;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CategoriesContainer {

    private String version;
    private LocalDate changeDate;
    private List<BookingCategory> bookingCategories;
}
