package laba16.task1;

public class EventLogger {
    private static int minutes = 0;

    public static void log(String source, String event) {
        int h = 10 + (minutes / 60);
        int m = minutes % 60;
        String time = String.format("%02d:%02d", h, m);
        System.out.printf("[%s] [%s] %s%n", time, source, event);
        minutes += 2;
    }
}