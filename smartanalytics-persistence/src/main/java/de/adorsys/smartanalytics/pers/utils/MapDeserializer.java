package de.adorsys.smartanalytics.pers.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Map;

public class MapDeserializer extends StdDeserializer<Map<String, String>> {

    private ObjectMapper mapper = new ObjectMapper();

    public MapDeserializer() {
        this(null);
    }

    @Override
    public Map<String, String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.getText() == null || p.getText().isEmpty()) {
            return null;
        }

        TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
        };

        return mapper.readValue(p.getText(), typeRef);
    }

    public MapDeserializer(Class<?> nominalType) {
        super(nominalType);
    }


}
