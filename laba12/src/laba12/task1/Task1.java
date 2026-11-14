package laba12.task1;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Допоміжний клас для логування у форматі [час] [джерело] подія
class EventLogger {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void log(String source, String event) {
        String time = LocalTime.now().format(TIME_FMT);
        System.out.printf("[%s] [%s] %s%n", time, source, event);
    }
}

// 1. Component (Спільний інтерфейс для окремого фільму та будь-якої групи)
interface FestivalComponent {
    String getName();
    int getDuration();      // Загальна тривалість у хвилинах
    int getMovieCount();    // Загальна кількість фільмів
    void printTree(String indent);
}

// 2. Leaf (Листок — окремий неподільний фільм)
class Movie implements FestivalComponent {
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

// 3. Composite (Компонувальник — група: Фестиваль, Тематичний блок або Збірка короткометражок)
class ProgramGroup implements FestivalComponent {
    private final String groupType; // Наприклад: "Фестиваль", "Блок", "Збірка"
    private final String name;
    private final List<FestivalComponent> children = new ArrayList<>();

    public ProgramGroup(String groupType, String name) {
        this.groupType = groupType;
        this.name = name;
        EventLogger.log("ProgramGroup", "Створено вузол [" + groupType + "]: \"" + name + "\"");
    }

    public void add(FestivalComponent component) {
        children.add(component);
        EventLogger.log("ProgramGroup", "До \"" + name + "\" додано \"" + component.getName() + "\"");
    }

    public void remove(FestivalComponent component) {
        children.remove(component);
        EventLogger.log("ProgramGroup", "З \"" + name + "\" видалено \"" + component.getName() + "\"");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDuration() {
        int total = 0;
        for (FestivalComponent child : children) {
            total += child.getDuration(); // Рекурсивний підрахунок незалежно від глибини
        }
        return total;
    }

    @Override
    public int getMovieCount() {
        int count = 0;
        for (FestivalComponent child : children) {
            count += child.getMovieCount();
        }
        return count;
    }

    @Override
    public void printTree(String indent) {
        System.out.printf("%s+ [%s] %s — разом: %d хв (фільмів: %d)%n",
                indent, groupType, name, getDuration(), getMovieCount());
        for (FestivalComponent child : children) {
            child.printTree(indent + "    ");
        }
    }
}

// 4. Client / Runner (Демонстраційний сценарій з методички)
public class Task1 {
    // Єдиний метод розрахунку показників — працює однаково для всього фестивалю, блоку, збірки чи одного фільму
    private static void printNodeSummary(FestivalComponent node) {
        int duration = node.getDuration();
        int movies = node.getMovieCount();
        EventLogger.log("Scheduler", String.format(
                "Розрахунок для \"%s\": тривалість = %d хв, кількість фільмів = %d",
                node.getName(), duration, movies));
    }

    public static void main(String[] args) {
        EventLogger.log("Main", "=== Старт формування програми кінофестивалю «Кадр» ===");

        // Створюємо окремі фільми (Leaf)
        Movie mainOpening = new Movie("Тіні забутих предків", 97);
        Movie dramaEvening = new Movie("Додому", 96);

        Movie docFilm = new Movie("20 днів у Маріуполі", 95);
        Movie short1 = new Movie("Вхід через балкон", 16);
        Movie short2 = new Movie("Паперовий літак", 12);
        Movie short3 = new Movie("Кінотеатр на околиці", 18);

        // Створюємо збірку короткометражок (Composite рівня 3)
        ProgramGroup shortsCollection = new ProgramGroup("Збірка короткометражок", "Нова українська хвиля");
        shortsCollection.add(short1);
        shortsCollection.add(short2);
        shortsCollection.add(short3);

        // Створюємо 2 тематичні блоки (Composite рівня 2)
        ProgramGroup block1 = new ProgramGroup("Тематичний блок", "Класика та драма");
        block1.add(mainOpening);
        block1.add(dramaEvening);

        ProgramGroup block2 = new ProgramGroup("Тематичний блок", "Документалістика та короткий метр");
        block2.add(docFilm);
        block2.add(shortsCollection); // Вкладаємо збірку всередину блоку

        // Створюємо сам фестиваль (Composite рівня 1)
        ProgramGroup festival = new ProgramGroup("Фестиваль", "Кінофестиваль «Кадр 2026»");
        festival.add(block1);
        festival.add(block2);

        System.out.println("\n--- СТРУКТУРА ПРОГРАМИ ФЕСТИВАЛЮ (ДЕРЕВО) ---");
        festival.printTree("");

        System.out.println("\n--- ПЕРЕВІРКА ОДНАКОВОГО ВИКЛИКУ ДЛЯ РІЗНИХ РІВНІВ ---");
        printNodeSummary(festival); // Виклик для всього фестивалю
        printNodeSummary(block2);   // Той самий виклик для одного блоку (зі збіркою всередині)
    }
}
