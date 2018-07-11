/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
