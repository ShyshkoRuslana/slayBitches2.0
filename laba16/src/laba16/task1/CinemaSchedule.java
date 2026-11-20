package laba16.task1;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CinemaSchedule {
    // Закрита внутрішня структура: Зала -> (Дата -> Список сеансів)
    private final Map<String, Map<LocalDate, List<Session>>> scheduleByHallAndDate = new LinkedHashMap<>();

    public void addSession(Session session) {
        scheduleByHallAndDate
                .computeIfAbsent(session.getHallName(), k -> new LinkedHashMap<>())
                .computeIfAbsent(session.getDate(), k -> new ArrayList<>())
                .add(session);
        EventLogger.log("CinemaSchedule", "Додано сеанс до розкладу: " + session);
    }

    // Пакетний метод доступу для ітераторів, щоб не відкривати структуру клієнтам
    List<Session> getAllSessionsInternal() {
        List<Session> all = new ArrayList<>();
        for (Map<LocalDate, List<Session>> byDate : scheduleByHallAndDate.values()) {
            for (List<Session> sessions : byDate.values()) {
                all.addAll(sessions);
            }
        }
        return all;
    }

    public SessionIterator createChronologicalIterator(LocalDate targetDate) {
        return new ChronologicalIterator(this, targetDate);
    }

    public SessionIterator createMovieIterator(String movieTitle) {
        return new MovieSessionIterator(this, movieTitle);
    }

    public SessionIterator createAvailableSeatsIterator() {
        return new AvailableSeatsIterator(this);
    }

    public void printInternalStructureSummary() {
        EventLogger.log("CinemaSchedule", "Поточний стан розкладу: залів = "
                + scheduleByHallAndDate.size() + ", всього сеансів = " + getAllSessionsInternal().size());
    }
}
