package com.springBoot.ewdj_2024_ep3_festivals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeFormatter implements Formatter<LocalTime> {

    @Autowired
    private MessageSource messageSource;

    public TimeFormatter() {
        super();
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return object.format(formatter(locale));
    }

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return LocalTime.parse(text, formatter(locale));
    }

    private DateTimeFormatter formatter(Locale locale) {
        return DateTimeFormatter.ofPattern(
                messageSource.getMessage("time.format.pattern", null, locale),
                locale);
    }
}
