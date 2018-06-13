package de.adorsys.smartanalytics.pers.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Map;

public class MapSerializer extends JsonSerializer<Map<String, String>> {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void serialize(Map<String, String> value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeRawValue(mapper.writeValueAsString(value));
    }
}
