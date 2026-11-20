package laba16.task2;

public class EventLogger {
    private static int minutes = 0;

    public static void log(String source, String event) {
        int h = 12 + (minutes / 60);
        int m = minutes % 60;
        System.out.printf("[%02d:%02d] [%s] %s%n", h, m, source, event);
        minutes += 1;
    }
}