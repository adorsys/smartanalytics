package de.adorsys.smartanalytics.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WrappedBooking implements Cloneable {

    protected Booking booking;

    // ******** rule provided fields **********
    private String mainCategory;
    private String subCategory;
    private String specification;
    private String receiver;
    private String logo;
    private String homepage;
    private String hotline;
    private String email;
    // *****************************************

    // ******** budget provided fields *********
    private LocalDate nextBookingDate;
    private boolean variable;
    private Cycle cycle;
    // *****************************************

    private Set<String> usedRules = new HashSet<>();

    public WrappedBooking(Booking booking) {
        this.booking = booking;
    }

    public String getReferenceName() {
        if (booking == null) {
            return null;
        }
        return booking.getReferenceName();
    }

    public String getCreditorId() {
        if (booking == null) {
            return null;
        }
        return booking.getCreditorId();
    }

    public String getPurpose() {
        if (booking == null) {
            return null;
        }
        return booking.getPurpose();
    }

    public String getIban() {
        if (booking == null) {
            return null;
        }
        return booking.getIban();
    }

    public String getAccountNumber() {
        if (booking == null) {
            return null;
        }
        return booking.getAccountNumber();
    }

    public String getBankCode() {
        if (booking == null) {
            return null;
        }
        return booking.getBankCode();
    }

    public BigDecimal getAmount() {
        if (booking == null) {
            return null;
        }
        return booking.getAmount();
    }

    public void setAmount(BigDecimal amount) {
        booking.setAmount(amount);
    }

    public LocalDate getExecutionDate() {
        if (booking == null) {
            return null;
        }
        return booking.getExecutionDate();
    }

    public String getMandateReference() {
        if (booking == null) {
            return null;
        }
        return booking.getMandateReference();
    }

    public Booking.BookingType getType() {
        if (booking == null) {
            return null;
        }
        return booking.getType();
    }

    public Set<String> getRuleIds() {
        if (usedRules.size() == 0) {
            return null;
        }
        return usedRules;
    }

    public Booking getBooking() {
        return booking;
    }

    public String getBankConnection() {
        if (booking == null) {
            return null;
        }
        if (getIban() != null) {
            return getIban();
        }
        if (StringUtils.isNotBlank(getBankCode()) && StringUtils.isNotBlank(getAccountNumber())) {
            return getBankCode() + getAccountNumber();
        }
        return getReferenceName();
    }

    public void putUsedRule(String id) {
        usedRules.add(id);
    }

    @Override
    public WrappedBooking clone() {
        try {
            WrappedBooking extension = (WrappedBooking) super.clone();
            extension.booking = this.booking.clone();
            return extension;
        } catch (CloneNotSupportedException e) {
            // should not be thrown
            throw new IllegalStateException(e);
        }
    }
}


