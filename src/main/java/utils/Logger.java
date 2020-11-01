package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    public static void logWithTime(String format, Object...args) {
        var now = LocalDateTime.now();
        log(now.format(DateTimeFormatter.ISO_DATE_TIME) + ": " + format, args);
    }

    public static void log(String format, Object...args) {
        System.out.printf(format + "\n", args);
    }
}
