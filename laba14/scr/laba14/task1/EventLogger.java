package laba14.task1;
public class EventLogger {
    private static int minutes = 0;

    public static void log(String source, String message) {
        int h = 14 + (minutes / 60);
        int m = minutes % 60;
        String time = String.format("%02d:%02d", h, m);
        System.out.printf("[%s] [%s] %s%n", time, source, message);
        minutes += 1;
    }
}
