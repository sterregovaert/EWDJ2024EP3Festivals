package utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import static utils.InitFormatter.DECIMAL_FORMAT;

public class TicketPriceSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String formattedValue = DECIMAL_FORMAT.format(value);
        gen.writeString(formattedValue);
    }
}
