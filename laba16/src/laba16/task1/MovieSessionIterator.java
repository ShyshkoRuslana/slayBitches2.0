package laba16.task1;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class MovieSessionIterator implements SessionIterator {
    private final List<Session> filteredSessions;
    private int cursor = 0;

    public MovieSessionIterator(CinemaSchedule schedule, String movieTitle) {
        this.filteredSessions = schedule.getAllSessionsInternal().stream()
                .filter(s -> s.getMovieTitle().equalsIgnoreCase(movieTitle))
                .sorted(Comparator.comparing(Session::getDate).thenComparing(Session::getTime))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasNext() {
        return cursor < filteredSessions.size();
    }

    @Override
    public Session next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Більше немає сеансів для цього фільму.");
        }
        return filteredSessions.get(cursor++);
    }
}
