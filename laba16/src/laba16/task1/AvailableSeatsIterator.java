package laba16.task1;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class AvailableSeatsIterator implements SessionIterator {
    private final List<Session> filteredSessions;
    private int cursor = 0;

    public AvailableSeatsIterator(CinemaSchedule schedule) {
        this.filteredSessions = schedule.getAllSessionsInternal().stream()
                .filter(s -> s.getAvailableSeats() > 0)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasNext() {
        return cursor < filteredSessions.size();
    }

    @Override
    public Session next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Більше немає сеансів з вільними місцями.");
        }
        return filteredSessions.get(cursor++);
    }
}
