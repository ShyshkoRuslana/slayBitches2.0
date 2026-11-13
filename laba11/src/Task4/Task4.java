package Task4;

// ==========================================
// 1. ПІДСИСТЕМИ ЗАЛИ (залишаються доступними окремо)
// ==========================================

class MovieLoader {
    public void loadCopy() {
        System.out.println("[Підсистема] Копію фільму завантажено на сервер.");
    }
}

class Projector {
    public void configure() {
        System.out.println("[Підсистема] Проектор налаштовано (роздільна здатність, фокус).");
    }
}

class SoundSystem {
    public void configure() {
        System.out.println("[Підсистема] Звукову систему налаштовано (формат 7.1, гучність).");
    }
}

class LightingSystem {
    public void turnOn() {
        System.out.println("[Підсистема] Освітлення увімкнено (аварійний / робочий режим).");
    }

    public void turnOff() {
        System.out.println("[Підсистема] Освітлення вимкнено для кінопоказу.");
    }
}

class TicketSales {
    public void openSales() {
        System.out.println("[Підсистема] Продаж квитків відкрито.");
    }
}

class AdvertisingSystem {
    public void enableAds() {
        System.out.println("[Підсистема] Рекламні ролики увімкнено.");
    }
}


// ==========================================
// 2. ФАСАД (об'єднує підсистеми в одну операцію)
// ==========================================

class CinemaFacade {
    private final MovieLoader movieLoader;
    private final Projector projector;
    private final SoundSystem soundSystem;
    private final LightingSystem lightingSystem;
    private final TicketSales ticketSales;
    private final AdvertisingSystem advertisingSystem;

    public CinemaFacade() {
        this.movieLoader = new MovieLoader();
        this.projector = new Projector();
        this.soundSystem = new SoundSystem();
        this.lightingSystem = new LightingSystem();
        this.ticketSales = new TicketSales();
        this.advertisingSystem = new AdvertisingSystem();
    }

    /**
     * Єдина проста операція для підготовки сеансу в правильному порядку:
     * 1. Завантаження фільму -> 2. Налаштування проектора -> 3. Налаштування звуку ->
     * 4. Вимкнення світла -> 5. Відкриття продажу -> 6. Увімкнення реклами
     */
    public void prepareShow() {
        System.out.println("--- ПОЧАТОК ПІДГОТОВКИ СЕАНСУ ---");
        movieLoader.loadCopy();
        projector.configure();
        soundSystem.configure();
        lightingSystem.turnOff(); // Спочатку вимикаємо світло, потім рекламу/показ
        ticketSales.openSales();
        advertisingSystem.enableAds();
        System.out.println("--- СЕАНС ПОВНІСТЮ ПІДГОТОВЛЕНО ---\n");
    }

    /**
     * Надання прямого доступу до підсистеми освітлення для окремих випадків (наприклад, аварія)
     */
    public LightingSystem getLightingSystem() {
        return lightingSystem;
    }
}


// ==========================================
// 3. ДЕМОНСТРАЦІЙНИЙ СЦЕНАРІЙ (Main)
// ==========================================

public class Task4 {
    public static void main(String[] args) {
        // Створюємо єдиний фасад кінотеатру
        CinemaFacade cinema = new CinemaFacade();

        System.out.println("=== СЦЕНАРІЙ 1: Адміністратор готує сеанс ===");
        cinema.prepareShow();

        System.out.println("=== СЦЕНАРІЙ 2: Автоматичний розклад готує наступний сеанс ===");
        cinema.prepareShow();

        System.out.println("=== СЦЕНАРІЙ 3: Окреме увімкнення світла в залі (аварійна ситуація) ===");
        cinema.getLightingSystem().turnOn();
    }
}
