package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import utils.InitFormatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class FormatterService implements InitFormatter {

    @Autowired
    private MessageSource messageSource;

    @Override
    public DateTimeFormatter getDateTimeFormatter() {
        String pattern = messageSource.getMessage("datetime.pattern", null, LocaleContextHolder.getLocale());
        return DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public DecimalFormat getDecimalFormatter() {
        String pattern = messageSource.getMessage("decimal.pattern", null, LocaleContextHolder.getLocale());
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(LocaleContextHolder.getLocale());
        return new DecimalFormat(pattern, symbols);
    }

    @Override
    public DateTimeFormatter getDateFormatter() {
        String pattern = messageSource.getMessage("date.format.pattern", null, LocaleContextHolder.getLocale());
        return DateTimeFormatter.ofPattern(pattern);
    }

    @Override
    public DateTimeFormatter getTimeFormatter() {
        String pattern = messageSource.getMessage("time.format.pattern", null, LocaleContextHolder.getLocale());
        return DateTimeFormatter.ofPattern(pattern);
    }
}