package utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static utils.InitFormatter.DECIMAL_FORMAT;

public class TicketPriceDeserializer extends JsonDeserializer<Double> {

    @Override
    public Double deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.getValueAsString();
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return DECIMAL_FORMAT.parse(value).doubleValue();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid value for ticket price field: " + value, e);
        }
    }

}



