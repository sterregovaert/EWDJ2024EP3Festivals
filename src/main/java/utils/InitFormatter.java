package utils;

import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public interface InitFormatter {
//        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.FRANCE));
//        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    DateTimeFormatter getDateTimeFormatter();

    DecimalFormat getDecimalFormatter();

    DateTimeFormatter getDateFormatter();

    DateTimeFormatter getTimeFormatter();
}