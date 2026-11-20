package laba16.task1;

import java.time.LocalDate;
import java.time.LocalTime;

public class Session {
    private final String movieTitle;
    private final String hallName;
    private final LocalDate date;
    private final LocalTime time;
    private final int availableSeats;

    public Session(String movieTitle, String hallName, LocalDate date, LocalTime time, int availableSeats) {
        this.movieTitle = movieTitle;
        this.hallName = hallName;
        this.date = date;
        this.time = time;
        this.availableSeats = availableSeats;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getHallName() {
        return hallName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    @Override
    public String toString() {
        return String.format("«%s» | %s | %s о %s | Вільних місць: %d",
                movieTitle, hallName, date, time, availableSeats);
    }
}
