package Task2;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Головний клас файлу (має збігатися з назвою файлу Task2.java)
public class Task2 {

    public static void runDemo() {
        System.out.println("=== ДЕМОНСТРАЦІЯ ЗАВДАННЯ 2: ТРЕЙЛЕРИ ФІЛЬМІВ (PROXY) ===\n");

        // 1. Створюємо афішу з 6 фільмами (через Proxy — жодного реального завантаження!)
        System.out.println("--- Крок 1: Відкриття афіші з 6 фільмами ---");
        MoviePosterPage posterPage = new MoviePosterPage();
        posterPage.addTrailer(new TrailerProxy("Дюна: Частина друга", "dune2_trailer.mp4", false));
        posterPage.addTrailer(new TrailerProxy("Думками навиворіт 2", "inside_out2.mp4", false));
        posterPage.addTrailer(new TrailerProxy("Дедпул і Росомаха", "deadpool3_18plus.mp4", true));
        posterPage.addTrailer(new TrailerProxy("Гладіатор 2", "gladiator2.mp4", false));
        posterPage.addTrailer(new TrailerProxy("Джокер: Божевілля на двох", "joker2_18plus.mp4", true));
        posterPage.addTrailer(new TrailerProxy("Інтерстеллар (перепоказ)", "interstellar.mp4", false));

        posterPage.showAllTitles();
        System.out.println();

        // Створюємо глядачів
        Viewer guest = new Viewer("Гість (неавторизований)", false);
        Viewer adultViewer = new Viewer("Олена (підтверджено 18+)", true);

        // 2. Глядач запускає звичайний трейлер двічі
        System.out.println("--- Крок 2: Запуск трейлера 'Дюна: Частина друга' двічі ---");
        posterPage.watchTrailer(0, guest); // Перший запуск -> завантаження з диска/сервера
        System.out.println();
        posterPage.watchTrailer(0, guest); // Повторний запуск -> миттєво без завантаження
        System.out.println();

        // 3. Неавторизований глядач запускає трейлер 18+
        System.out.println("--- Крок 3: Неавторизований глядач запускає трейлер 18+ ---");
        posterPage.watchTrailer(2, guest); // Відмова! Файл навіть не завантажується
        System.out.println();

        // 4. Повнолітній користувач запускає той самий трейлер 18+
        System.out.println("--- Крок 4: Авторизований повнолітній глядач запускає трейлер 18+ ---");
        posterPage.watchTrailer(2, adultViewer); // Доступ дозволено -> завантаження і показ
    }

    public static void main(String[] args) {
        runDemo();
    }
}

// Утиліта для журналювання подій у форматі [час] [джерело] подія
class EventLogger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void log(String source, String message) {
        String time = LocalTime.now().format(FORMATTER);
        System.out.printf("[%s] [%s] %s%n", time, source, message);
    }
}

// Модель користувача (глядача)
record Viewer(String name, boolean isAdultVerified) {
}

// 1. Спільний інтерфейс (Subject)
interface ITrailer {
    String getMovieTitle();
    void play(Viewer viewer);
}

// 2. Реальний важкий об'єкт відеофайлу (RealSubject)
class RealTrailer implements ITrailer {
    private final String movieTitle;
    private final String fileName;

    public RealTrailer(String movieTitle, String fileName) {
        this.movieTitle = movieTitle;
        this.fileName = fileName;
        loadFromStorage(); // Імітація важкого завантаження при створенні об'єкта
    }

    private void loadFromStorage() {
        EventLogger.log("RealTrailer:" + fileName,
                "ЗАВАНТАЖЕННЯ великого відеофайлу (450 МБ) зі сховища для фільму «" + movieTitle + "»...");
    }

    @Override
    public String getMovieTitle() {
        return movieTitle;
    }

    @Override
    public void play(Viewer viewer) {
        EventLogger.log("RealTrailer:" + fileName,
                "Відтворення відео для глядача: " + viewer.name());
    }
}

// 3. Замісник (Proxy): контролює доступ 18+ та відкладає завантаження
class TrailerProxy implements ITrailer {
    private final String movieTitle;
    private final String fileName;
    private final boolean isRestricted18Plus;
    private RealTrailer realTrailer; // Посилання на реальний об'єкт (спочатку null)

    public TrailerProxy(String movieTitle, String fileName, boolean isRestricted18Plus) {
        this.movieTitle = movieTitle;
        this.fileName = fileName;
        this.isRestricted18Plus = isRestricted18Plus;
        EventLogger.log("TrailerProxy",
                "Створено легку обгортку для «" + movieTitle + "» (18+: " + isRestricted18Plus + ") без завантаження відео.");
    }

    @Override
    public String getMovieTitle() {
        return movieTitle;
    }

    @Override
    public void play(Viewer viewer) {
        EventLogger.log("TrailerProxy",
                "Запит на перегляд трейлера «" + movieTitle + "» від користувача: " + viewer.name());

        // 1. Перевірка прав доступу (Protection Proxy)
        if (isRestricted18Plus && !viewer.isAdultVerified()) {
            EventLogger.log("TrailerProxy",
                    "ВІДМОВА: Перегляд доступний лише після підтвердження віку.");
            return;
        }

        // 2. Лінива ініціалізація (Virtual Proxy) — завантажуємо лише один раз при першому запуску
        if (realTrailer == null) {
            EventLogger.log("TrailerProxy",
                    "Трейлер ще не в пам'яті. Ініціюємо перше завантаження...");
            realTrailer = new RealTrailer(movieTitle, fileName);
        } else {
            EventLogger.log("TrailerProxy",
                    "Трейлер уже завантажений у пам'ять. Повторне завантаження не потрібне.");
        }

        // 3. Делегування відтворення реальному об'єкту
        realTrailer.play(viewer);
    }
}

// 4. Клієнт — Сторінка афіші (Client)
class MoviePosterPage {
    private final List<ITrailer> trailers = new ArrayList<>();

    public void addTrailer(ITrailer trailer) {
        trailers.add(trailer);
    }

    public void showAllTitles() {
        EventLogger.log("MoviePosterPage", "Відображення списку з " + trailers.size() + " фільмів на афіші:");
        for (int i = 0; i < trailers.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + trailers.get(i).getMovieTitle());
        }
    }

    public void watchTrailer(int index, Viewer viewer) {
        if (index >= 0 && index < trailers.size()) {
            trailers.get(index).play(viewer);
        }
    }
}
