package utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public interface InitFormatter {

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.00", DecimalFormatSymbols.getInstance(Locale.getDefault()));
}