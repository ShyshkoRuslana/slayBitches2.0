package laba12.task2;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Допоміжний клас для логування у форматі [час] [джерело] подія
class EventLogger {
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void log(String source, String event) {
        String time = LocalTime.now().format(TIME_FMT);
        System.out.printf("[%s] [%s] %s%n", time, source, event);
    }
}

// 1. Flyweight (Спільні незмінні дані категорії місця)
class SeatCategory {
    private final String name;
    private final String description;
    private final int chairWidthCm;
    private final String iconPath;
    private final double basePrice;

    public SeatCategory(String name, String description, int chairWidthCm, String iconPath, double basePrice) {
        this.name = name;
        this.description = description;
        this.chairWidthCm = chairWidthCm;
        this.iconPath = iconPath;
        this.basePrice = basePrice;
        EventLogger.log("SeatCategory", "Створено новий об'єкт категорії: \"" + name + "\"");
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return String.format("%s (%s, ширина: %d см, іконка: %s, базова ціна: %.2f грн)",
                name, description, chairWidthCm, iconPath, basePrice);
    }
}

// 2. Flyweight Factory (Кеш/фабрика, що створює кожну категорію лише 1 раз)
class SeatCategoryFactory {
    private static final Map<String, SeatCategory> categories = new HashMap<>();

    public static SeatCategory getCategory(String type) {
        SeatCategory category = categories.get(type);
        if (category == null) {
            switch (type) {
                case "Стандарт":
                    category = new SeatCategory("Стандарт", "Звичайне м'яке крісло", 52, "icons/standard.png", 150.0);
                    break;
                case "Комфорт":
                    category = new SeatCategory("Комфорт", "Збільшений простір для ніг, підсклянник", 60, "icons/comfort.png", 210.0);
                    break;
                case "VIP":
                    category = new SeatCategory("VIP", "Шкіряне крісло-реклайнер із столиком", 75, "icons/vip.png", 350.0);
                    break;
                default:
                    throw new IllegalArgumentException("Невідома категорія місця: " + type);
            }
            categories.put(type, category);
        }
        return category;
    }

    public static int getCreatedCategoriesCount() {
        return categories.size();
    }
}

// 3. Context (Конкретне місце на сеансі — зберігає лише унікальні дані + посилання на категорію)
class Seat {
    private final int sessionId;
    private final int row;
    private final int number;
    private boolean isSold;
    private final SeatCategory category;

    public Seat(int sessionId, int row, int number, boolean isSold, SeatCategory category) {
        this.sessionId = sessionId;
        this.row = row;
        this.number = number;
        this.isSold = isSold;
        this.category = category;
    }

    public SeatCategory getCategory() {
        return category;
    }

    public String getFullDescription() {
        String status = isSold ? "Продано" : "Вільно";
        return String.format("Сеанс #%d | Ряд %d, Місце %d | Статус: %s | Категорія: %s",
                sessionId, row, number, status, category.getDetails());
    }
}

// 4. Client / Runner (Головний клас для запуску демонстраційного сценарію)
public class task2 {
    public static void main(String[] args) {
        EventLogger.log("Main", "=== Старт формування схем місць для 200 сеансів ===");

        List<Seat> allSeats = new ArrayList<>();
        int totalSessions = 200;
        int rowsPerHall = 10;
        int seatsPerRow = 18; // 180 місць у залі -> 36 000 місць за 200 сеансів

        Seat vipSeatSession1 = null;
        Seat vipSeatSession150 = null;

        for (int sessionId = 1; sessionId <= totalSessions; sessionId++) {
            for (int row = 1; row <= rowsPerHall; row++) {
                for (int num = 1; num <= seatsPerRow; num++) {
                    String categoryType;
                    if (row <= 6) {
                        categoryType = "Стандарт";
                    } else if (row <= 9) {
                        categoryType = "Комфорт";
                    } else {
                        categoryType = "VIP";
                    }

                    // Отримуємо спільний об'єкт категорії через фабрику (створиться лише 3 рази!)
                    SeatCategory sharedCategory = SeatCategoryFactory.getCategory(categoryType);
                    boolean isSold = (sessionId + row + num) % 5 == 0;

                    Seat seat = new Seat(sessionId, row, num, isSold, sharedCategory);
                    allSeats.add(seat);

                    // Зберігаємо два VIP-місця з різних сеансів для перевірки в кінці
                    if (sessionId == 1 && row == 10 && num == 5) {
                        vipSeatSession1 = seat;
                    }
                    if (sessionId == 150 && row == 10 && num == 12) {
                        vipSeatSession150 = seat;
                    }
                }
            }
        }

        EventLogger.log("Main", "Формування схем завершено.");
        EventLogger.log("Stats", "Загальна кількість створених об'єктів місць (Seat): " + allSeats.size());
        EventLogger.log("Stats", "Кількість створених об'єктів категорій (SeatCategory): "
                + SeatCategoryFactory.getCreatedCategoriesCount());

        System.out.println("\n--- ПЕРЕВІРКА ПОСИЛАННЯ НА СПІЛЬНИЙ ОБ'ЄКТ КАТЕГОРІЇ ---");
        if (vipSeatSession1 != null && vipSeatSession150 != null) {
            EventLogger.log("Check", "Місце 1: " + vipSeatSession1.getFullDescription());
            EventLogger.log("Check", "Місце 2: " + vipSeatSession150.getFullDescription());

            boolean isSameObject = (vipSeatSession1.getCategory() == vipSeatSession150.getCategory());
            EventLogger.log("Check", "Посилаються на той самий об'єкт SeatCategory у пам'яті (==): "
                    + isSameObject + " (hashCode: " + System.identityHashCode(vipSeatSession1.getCategory()) + ")");
        }
    }
}