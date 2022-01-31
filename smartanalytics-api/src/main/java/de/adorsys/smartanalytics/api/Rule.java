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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.Map;

@Data
@JsonPropertyOrder({"order", "ruleId", "creditorId", "similarityMatchType", "expression", "receiver", "stop", "incoming", "mainCategory", "subCategory", "specification", "custom", "email", "homepage", "hotline", "logo"})
public class Rule {

    public enum SIMILARITY_MATCH_TYPE {
        IBAN, REFERENCE_NAME, PURPOSE, CUSTOM
    }

    private int order;
    private String ruleId;
    private String mainCategory;
    private String subCategory;
    private String specification;

    private SIMILARITY_MATCH_TYPE similarityMatchType;
    private String creditorId;
    private String expression;
    private String receiver;

    private String logo;
    private String hotline;
    private String homepage;
    private String email;

    private Map<String, String> custom;

    private boolean stop;
    private boolean incoming;
}
