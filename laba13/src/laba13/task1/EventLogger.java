package laba13.task1;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EventLogger {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void log(String source, String event) {
        String time = LocalTime.now().format(TIME_FORMATTER);
        System.out.println("[" + time + "] [" + source + "] " + event);
    }
}