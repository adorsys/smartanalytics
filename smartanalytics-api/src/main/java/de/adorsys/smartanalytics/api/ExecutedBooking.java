package de.adorsys.smartanalytics.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecutedBooking {

    private String bookingId;
    private LocalDate executionDate;
    private boolean executed;

}
