/*
 * Copyright 2018-2022 adorsys GmbH & Co KG
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version. This program is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see https://www.gnu.org/licenses/.
 *
 * This project is also available under a separate commercial license. You can
 * contact us at psd2@adorsys.com.
 */

package de.adorsys.smartanalytics.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WrappedBooking implements Cloneable {

    protected Booking booking;

    // ******** categorization provided fields **********
    private String mainCategory;
    private String subCategory;
    private String specification;
    private String otherAccount;
    private String logo;
    private String homepage;
    private String hotline;
    private String email;
    private Map<String, String> custom;
    private Set<String> usedRules = new HashSet<>();
    // *****************************************

    // ******** classification provided fields *********
    private LocalDate nextBookingDate;
    private Cycle cycle;
    // *****************************************

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

    public boolean isStandingOrder() {
        if (booking == null) {
            return false;
        }
        return booking.isStandingOrder();
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
        if (getBankCode() != null && getBankCode().length() > 0 && getAccountNumber() != null && getAccountNumber().length() > 0) {
            return getBankCode() + getAccountNumber();
        }
        return getReferenceName();
    }

    public void applyRule(Rule rule) {
        setMainCategory(rule.getMainCategory());
        setSubCategory(rule.getSubCategory());
        setSpecification(rule.getSpecification());
        setEmail(rule.getEmail());
        setHomepage(rule.getHomepage());
        setLogo(rule.getLogo());
        setOtherAccount(rule.getReceiver());
        setHotline(rule.getHotline());
        setCustom(rule.getCustom());
        putUsedRule(rule.getRuleId());
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


