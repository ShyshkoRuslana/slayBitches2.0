package laba15.task3;
import java.util.List;

// 1. Базовий абстрактний клас із шаблонним методом
abstract class MovieSession {
    private final String movieTitle;
    private final String sessionType;

    public MovieSession(String movieTitle, String sessionType) {
        this.movieTitle = movieTitle;
        this.sessionType = sessionType;
    }

    // ШАБЛОННИЙ МЕТОД (Template Method)
    // Ключове слово 'final' забороняє спадкоємцям змінювати порядок етапів
    public final void closeSession() {
        log("--- Початок процедури закриття сеансу ---");

        turnOnLights();
        letViewersOut();
        collectAdditionalItems();      // Необов'язковий етап (хук 1)
        inspectHall();
        cleanHall();
        prepareForNextSession();
        prepareAdditionalComfort();    // Необов'язковий етап (хук 2)
        generateReport();              // Єдиний формат звіту для всіх

        log("--- Сеанс успішно закрито ---\n");
    }

    // Спільні обов'язкові етапи (private, щоб ніхто не міг їх змінити)
    private void turnOnLights() {
        log("Увімкнено основне освітлення в залі.");
    }

    private void letViewersOut() {
        log("Відчинено вихідні двері, глядачі залишають зал.");
    }

    private void inspectHall() {
        log("Проведено перевірку залу на забуті речі та пошкодження.");
    }

    private void cleanHall() {
        log("Виконано стандартне прибирання крісел та проходів.");
    }

    private void prepareForNextSession() {
        log("Налаштовано проєктор та звук для наступного сеансу.");
    }

    private void generateReport() {
        log("ЗВІТ: Сеанс завершено | Формат: " + sessionType + " | Фільм: «" + movieTitle + "» | Статус: ГОТОВО.");
    }

    // Необов'язкові етапи — методи-перехоплювачі (Hooks)
    // Мають порожню реалізацію за замовчуванням; спадкоємці перевизначають їх лише за потреби
    protected void collectAdditionalItems() {
        // За замовчуванням нічого додаткового збирати не треба
    }

    protected void prepareAdditionalComfort() {
        // За замовчуванням додаткова підготовка комфорту не потрібна
    }

    // Допоміжний метод для запису в журнал
    protected void log(String message) {
        System.out.println("[" + sessionType + "] " + message);
    }
}

// 2. Клас для 2D-сеансу (не має специфічних етапів, використовує базову поведінку)
class Session2D extends MovieSession {
    public Session2D(String movieTitle) {
        super(movieTitle, "2D");
    }
}

// 3. Клас для 3D-сеансу (додає лише збір окулярів)
class Session3D extends MovieSession {
    public Session3D(String movieTitle) {
        super(movieTitle, "3D");
    }

    @Override
    protected void collectAdditionalItems() {
        log("Додатковий етап: зібрано 3D-окуляри біля виходу та відправлено на дезінфекцію.");
    }
}

// 4. Клас для VIP-сеансу (додає збір посуду та заміну пледів)
class SessionVIP extends MovieSession {
    public SessionVIP(String movieTitle) {
        super(movieTitle, "VIP");
    }

    @Override
    protected void collectAdditionalItems() {
        log("Додатковий етап: зібрано скляний посуд та тарілки зі столиків.");
    }

    @Override
    protected void prepareAdditionalComfort() {
        log("Додатковий етап: замінено пледи та подушки на кріслах.");
    }
}

// 5. Демонстраційний сценарій
public class Task3 {
    public static void main(String[] args) {
        List<MovieSession> sessionsToClose = List.of(
                new Session2D("Дюна: Частина друга"),
                new Session3D("Аватар: Шлях води"),
                new SessionVIP("Оппенгеймер")
        );

        // Закриваємо всі сеанси однією і тією самою процедурою
        for (MovieSession session : sessionsToClose) {
            session.closeSession();
        }
    }
}
