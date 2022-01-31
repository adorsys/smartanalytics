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

import de.adorsys.smartanalytics.api.config.Group;
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

    private Group.Type groupType;
    private String name;
    private boolean salaryWage;

    protected Cycle cycle;
    private BigDecimal amount;
    private List<BookingPeriod> bookingPeriods;

    private String mainCategory;
    private String subCategory;
    private String specification;
    private String email;
    private String hotline;
    private String homepage;
    private String logo;

    private boolean cancelled;
    private boolean contract;

    private String mandatreference;
    private String otherAccount;

    public BookingGroup(String firstKey, String secondKey, String name, Group.Type groupType) {
        this.firstKey = firstKey;
        this.secondKey = secondKey;
        this.name = name;
        this.groupType = groupType;
    }

    /**
     * Calculate the effectiveness of the category.
     *
     * @param referenceDate - a date on that the category is effective
     * @param bookings      - bookings to consider
     * @return true if the time difference between given date and
     * youngest booking is in the valid range of the category cycle, otherwise false
     */
    public boolean isEffective(LocalDate referenceDate, List<WrappedBooking> bookings) {
        return cycle != null && (cycle.isValid(bookings.get(bookings.size() - 1).getExecutionDate(), referenceDate));
    }

    public boolean isIncome() {
        return groupType == Group.Type.RECURRENT_INCOME || groupType == Group.Type.OTHER_INCOME;
    }

    public boolean isSalaryWageGroup() {
        return groupType == Group.Type.RECURRENT_INCOME && subCategory != null &&
            (subCategory.equals("EINKOMMEN") || subCategory.equals("RENTE"));
    }

    public boolean isRecurrent() {
        return groupType != Group.Type.CUSTOM && groupType != Group.Type.OTHER_INCOME &&
            groupType != Group.Type.OTHER_EXPENSES;
    }
}
