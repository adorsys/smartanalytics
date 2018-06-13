package de.adorsys.smartanalytics.pers.utils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

public class RuleMixIn {

    @JsonSerialize(using = MapSerializer.class)
    @JsonDeserialize(using = MapDeserializer.class)
    private Map<String, String> custom;

}