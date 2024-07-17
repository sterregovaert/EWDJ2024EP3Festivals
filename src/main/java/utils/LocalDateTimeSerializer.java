package utils;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import service.FormatterService;

import static utils.InitFormatter.*;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Autowired
    private FormatterService formatterService;

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(value.format(formatterService.getDateTimeFormatter()));
    }
}