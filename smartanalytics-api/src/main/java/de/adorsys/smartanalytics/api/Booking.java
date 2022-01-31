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

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking implements Cloneable {

    private String bookingId;
    private String referenceName;
    private String purpose;
    private BigDecimal amount;
    private LocalDate executionDate;
    private String creditorId;
    private boolean standingOrder;
    private String iban;
    private String accountNumber;
    private String bankCode;
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
