package laba15.task1;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// 1. Інтерфейс Стратегії (Strategy)
interface PricingStrategy {
    String getStrategyName();
    double calculatePrice(double basePrice, double occupancyRate, int ticketCount);
}

// 2. Конкретні Стратегії (Concrete Strategies)

// Стандартна ціна
class StandardPricingStrategy implements PricingStrategy {
    @Override
    public String getStrategyName() {
        return "Стандартний тариф";
    }

    @Override
    public double calculatePrice(double basePrice, double occupancyRate, int ticketCount) {
        return basePrice * ticketCount;
    }
}

// Динамічна ціна (залежно від заповненості зали)
class DynamicPricingStrategy implements PricingStrategy {
    @Override
    public String getStrategyName() {
        return "Динамічний тариф (залежно від заповненості)";
    }

    @Override
    public double calculatePrice(double basePrice, double occupancyRate, int ticketCount) {
        double multiplier = 1.0;
        if (occupancyRate >= 0.80) {
            multiplier = 1.30; // +30%, якщо зала заповнена на 80% і більше
        } else if (occupancyRate >= 0.50) {
            multiplier = 1.15; // +15%, якщо зала заповнена на 50% і більше
        }
        return basePrice * multiplier * ticketCount;
    }
}

// Ранкові сеанси (знижка 25%)
class MorningPricingStrategy implements PricingStrategy {
    @Override
    public String getStrategyName() {
        return "Ранковий тариф (-25%)";
    }

    @Override
    public double calculatePrice(double basePrice, double occupancyRate, int ticketCount) {
        return basePrice * 0.75 * ticketCount;
    }
}

// Корпоративні замовлення (знижка 20% при замовленні від 10 квитків)
class CorporatePricingStrategy implements PricingStrategy {
    @Override
    public String getStrategyName() {
        return "Корпоративний тариф";
    }

    @Override
    public double calculatePrice(double basePrice, double occupancyRate, int ticketCount) {
        double discount = (ticketCount >= 10) ? 0.80 : 0.90;
        return basePrice * discount * ticketCount;
    }
}

// Нова стратегія, додана одним класом без зміни коду Session: «Прем'єра» (+50% до ціни)
class PremierePricingStrategy implements PricingStrategy {
    @Override
    public String getStrategyName() {
        return "Тариф «Прем'єра» (+50%)";
    }

    @Override
    public double calculatePrice(double basePrice, double occupancyRate, int ticketCount) {
        return basePrice * 1.50 * ticketCount;
    }
}

// 3. Допоміжний клас для логування у форматі [час] [джерело] подія
class EventLogger {
    private static int minutesOffset = 0;

    public static void log(String source, String event) {
        LocalTime time = LocalTime.of(10, 0).plusMinutes(minutesOffset);
        minutesOffset += 5;
        String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        System.out.printf("[%s] [%s] %s%n", formattedTime, source, event);
    }
}

// 4. Контекст (Context) — Сеанс у кінотеатрі
class Session {
    private final String movieTitle;
    private final double basePrice;
    private double occupancyRate; // від 0.0 до 1.0
    private PricingStrategy pricingStrategy;

    public Session(String movieTitle, double basePrice, double occupancyRate, PricingStrategy initialStrategy) {
        this.movieTitle = movieTitle;
        this.basePrice = basePrice;
        this.occupancyRate = occupancyRate;
        this.pricingStrategy = initialStrategy;
        EventLogger.log("Session: " + movieTitle,
                String.format("Створено сеанс (базова ціна: %.2f грн, заповненість: %.0f%%). Обрано алгоритм: [%s]",
                        basePrice, occupancyRate * 100, pricingStrategy.getStrategyName()));
    }

    public void setPricingStrategy(PricingStrategy newStrategy) {
        this.pricingStrategy = newStrategy;
        EventLogger.log("Admin -> Session: " + movieTitle,
                "Змінено алгоритм ціноутворення на: [" + pricingStrategy.getStrategyName() + "]");
    }

    public void setOccupancyRate(double occupancyRate) {
        this.occupancyRate = occupancyRate;
        EventLogger.log("Session: " + movieTitle,
                String.format("Оновлено заповненість зали: %.0f%%", occupancyRate * 100));
    }

    public double calculateOrderCost(int ticketCount) {
        double total = pricingStrategy.calculatePrice(basePrice, occupancyRate, ticketCount);
        EventLogger.log("TicketOffice",
                String.format("Розрахунок для %d квитків на «%s» за алгоритмом [%s] -> Сума: %.2f грн",
                        ticketCount, movieTitle, pricingStrategy.getStrategyName(), total));
        return total;
    }
}

// 5. Клас-раннер для відтворення демонстраційного сценарію
public class task1 {

    public static void runTask1() {
        // 1. Розрахувати ціну квитка за стандартним і динамічним правилом (зала заповнена на 80%)
        Session duneSession = new Session("Дюна: Частина друга", 200.0, 0.80, new StandardPricingStrategy());
        duneSession.calculateOrderCost(1);

        duneSession.setPricingStrategy(new DynamicPricingStrategy());
        duneSession.calculateOrderCost(1);

        // 2. Змінити правило сеансу на «корпоративне» і повторити розрахунок
        duneSession.setPricingStrategy(new CorporatePricingStrategy());
        duneSession.calculateOrderCost(12); // корпоративне замовлення на 12 квитків

        // 3. Додати правило «прем'єра» одним класом і розрахувати за ним
        duneSession.setPricingStrategy(new PremierePricingStrategy());
        duneSession.calculateOrderCost(2);
    }

    public static void main(String[] args) {
        runTask1();
    }
}
