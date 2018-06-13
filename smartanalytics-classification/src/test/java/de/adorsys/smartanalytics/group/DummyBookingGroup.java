package de.adorsys.smartanalytics.group;

import de.adorsys.smartanalytics.api.BookingGroup;
import de.adorsys.smartanalytics.api.Cycle;
import de.adorsys.smartanalytics.api.WrappedBooking;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DummyBookingGroup extends BookingGroup {

    private BigDecimal amount;
    private boolean effective;

    public DummyBookingGroup(String id) {
        super(id, id, "dummy", null);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public BigDecimal getAmount() {
        return this.amount;
    }

    @Override
    public boolean isEffective(LocalDate today, List<WrappedBooking> bookings) {
        return effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Dummy");
        sb.append(super.toString());
        sb.append(" with (");
        sb.append(amount).append(";");
        sb.append(effective);
        sb.append(")");
        return sb.toString();
    }


}
