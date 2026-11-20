package laba16.task1;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class ChronologicalIterator implements SessionIterator {
    private final List<Session> filteredSessions;
    private int cursor = 0;

    public ChronologicalIterator(CinemaSchedule schedule, LocalDate targetDate) {
        this.filteredSessions = schedule.getAllSessionsInternal().stream()
                .filter(s -> s.getDate().equals(targetDate))
                .sorted(Comparator.comparing(Session::getTime))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasNext() {
        return cursor < filteredSessions.size();
    }

    @Override
    public Session next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Більше немає сеансів на вказану дату.");
        }
        return filteredSessions.get(cursor++);
    }
}
