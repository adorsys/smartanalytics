package de.adorsys.smartanalytics.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class BookingGroup {

    private String firstKey;
    private String secondKey;

    protected Cycle cycle;
    private boolean variable = false;
    private BigDecimal amount;
    private List<LocalDate> bookingDates;
    private LocalDate nextExecutionDate;
    private Booking.BookingType bookingType;

    private String mainCategory;
    private String subCategory;
    private String specification;
    private String email;
    private String hotline;
    private String homepage;
    private String logo;
    private boolean contract;

    private String mandatreference;
    private String provider;

    public BookingGroup(String firstKey, String secondKey, Booking.BookingType bookingType) {
        this.firstKey = firstKey;
        this.secondKey = secondKey;
        this.bookingType = bookingType;
    }

    /**
     * Calculate the effectiveness of the category.
     *
     * @param referenceDate
     * @return true if the time difference between given date and
     * youngest booking is in the valid range of the category cycle otherwise false
     */
    public boolean isEffective(LocalDate referenceDate, List<WrappedBooking> bookings) {
        return cycle != null && (cycle.isValid(bookings.get(bookings.size() - 1).getExecutionDate(), referenceDate));
    }
}
