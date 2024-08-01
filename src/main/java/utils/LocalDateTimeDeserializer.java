package utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

import static utils.InitFormatter.DATE_TIME_FORMATTER;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String valueAsString = p.getValueAsString();
        if (valueAsString == null || valueAsString.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(valueAsString, DATE_TIME_FORMATTER);
    }
}
