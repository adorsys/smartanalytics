package de.adorsys.smartanalytics.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class BookingPeriod {

    private LocalDate start;
    private LocalDate end;
    private List<LocalDate> bookingDates;
}
