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
