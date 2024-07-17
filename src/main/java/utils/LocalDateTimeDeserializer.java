package utils;

import static utils.InitFormatter.*;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import service.FormatterService;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Autowired
    private FormatterService formatterService;

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String valueAsString = p.getValueAsString();
        if (valueAsString == null || valueAsString.isBlank()) {
            return null;
        }

        return LocalDateTime.parse(valueAsString, formatterService.getDateTimeFormatter());
    }
}
