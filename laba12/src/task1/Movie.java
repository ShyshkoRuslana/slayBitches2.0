package laba12.task1;

public class Movie implements FestivalComponent {
    private final String title;
    private final int durationMinutes;

    public Movie(String title, int durationMinutes) {
        this.title = title;
        this.durationMinutes = durationMinutes;
        EventLogger.log("Movie", "Створено фільм: \"" + title + "\" (" + durationMinutes + " хв)");
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public int getDuration() {
        return durationMinutes;
    }

    @Override
    public int getMovieCount() {
        return 1;
    }

    @Override
    public void printTree(String indent) {
        System.out.printf("%s- Фільм: \"%s\" (%d хв)%n", indent, title, durationMinutes);
    }
}