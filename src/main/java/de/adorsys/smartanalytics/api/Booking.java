package de.adorsys.smartanalytics.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking implements Cloneable {

    public enum BookingType {
        STANDING_ORDER,
        INCOME,
        DEBIT
    }

    private String bookingId;
    private String referenceName;
    private String creditorId;
    private String purpose;
    private String iban;
    private String accountNumber;
    private String bankCode;
    private BigDecimal amount;
    private LocalDate executionDate;
    private LocalDate orderDate;
    private BookingType type;
    private String mandateReference;

    @Override
    public Booking clone() {
        try {
            return (Booking) super.clone();
        } catch (CloneNotSupportedException e) {
            // should not be thrown
            throw new IllegalStateException(e);
        }
    }
}
