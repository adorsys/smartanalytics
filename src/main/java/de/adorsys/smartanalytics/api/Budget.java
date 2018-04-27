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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    private LocalDate analyticsDate;
    private BigDecimal incomeTotal = new BigDecimal(0);

    private BigDecimal incomeFix = new BigDecimal(0);
    private List<BookingGroup> incomeFixBookings = new ArrayList<>();
    private BigDecimal incomeVariable = new BigDecimal(0);
    private List<BookingGroup> incomeVariableBookings = new ArrayList<>();
    private BigDecimal incomeNext = new BigDecimal(0);
    private List<BookingGroup> incomeNextBookings = new ArrayList<>();

    private BigDecimal expensesTotal = new BigDecimal(0);

    private BigDecimal expensesFix = new BigDecimal(0);
    private List<BookingGroup> expensesFixBookings = new ArrayList<>();
    private BigDecimal expensesVariable = new BigDecimal(0);
    private List<BookingGroup> expensesVariableBookings = new ArrayList<>();
    private BigDecimal expensesNext = new BigDecimal(0);
    private List<BookingGroup> expensesNextBookings = new ArrayList<>();
}
