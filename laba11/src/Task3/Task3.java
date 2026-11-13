package Task3;

// 1. Базовий інтерфейс (Component)
interface Session {
    String getDescription();
    double getPrice();
}

// 2. Конкретний компонент (Concrete Component)
class BaseSession implements Session {
    private final String movieTitle;
    private final double basePrice;

    public BaseSession(String movieTitle, double basePrice) {
        this.movieTitle = movieTitle;
        this.basePrice = basePrice;
    }

    @Override
    public String getDescription() {
        return "Фільм: «" + movieTitle + "» (Стандартний сеанс)";
    }

    @Override
    public double getPrice() {
        return basePrice;
    }
}

// 3. Абстрактний декоратор (Base Decorator)
abstract class SessionDecorator implements Session {
    protected final Session wrappedSession;

    public SessionDecorator(Session session) {
        this.wrappedSession = session;
    }

    @Override
    public String getDescription() {
        return wrappedSession.getDescription();
    }

    @Override
    public double getPrice() {
        return wrappedSession.getPrice();
    }
}

// 4. Конкретні декоратори (Concrete Decorators)

// Модифікація: 3D (+50 грн)
class ThreeDDecorator extends SessionDecorator {
    public ThreeDDecorator(Session session) {
        super(session);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + [3D]";
    }

    @Override
    public double getPrice() {
        return super.getPrice() + 50.0;
    }
}

// Модифікація: Субтитри (+15 грн)
class SubtitlesDecorator extends SessionDecorator {
    public SubtitlesDecorator(Session session) {
        super(session);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + [Субтитри]";
    }

    @Override
    public double getPrice() {
        return super.getPrice() + 15.0;
    }
}

// Модифікація: Сурдопереклад (+20 грн)
class SignLanguageDecorator extends SessionDecorator {
    public SignLanguageDecorator(Session session) {
        super(session);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + [Сурдопереклад]";
    }

    @Override
    public double getPrice() {
        return super.getPrice() + 20.0;
    }
}

// Модифікація: «Тихий сеанс» для глядачів з дітьми (0 грн)
class QuietSessionDecorator extends SessionDecorator {
    public QuietSessionDecorator(Session session) {
        super(session);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + [Тихий сеанс (для батьків з дітьми)]";
    }

    @Override
    public double getPrice() {
        return super.getPrice();
    }
}

// Модифікація: Знижка вихідного дня (-20% від поточної вартості)
class WeekendDiscountDecorator extends SessionDecorator {
    public WeekendDiscountDecorator(Session session) {
        super(session);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + [Знижка вихідного дня -20%]";
    }

    @Override
    public double getPrice() {
        return super.getPrice() * 0.8;
    }
}

// 5. Демонстраційний клас
public class Task3 {

    // Система продажу працює виключно через інтерфейс Session
    public static void printTicketInfo(Session session) {
        System.out.println("Афіша: " + session.getDescription());
        System.out.printf("Вартість квитка: %.2f грн\n", session.getPrice());
        System.out.println("-".repeat(60));
    }

    public static void main(String[] args) {
        // Сценарій 1: Вивести базовий сеанс з ціною
        Session basicSession = new BaseSession("Дюна: Частина друга", 150.0);
        printTicketInfo(basicSession);

        // Сценарій 2: Створити сеанс 3D із субтитрами -> вивести опис і ціну
        Session session3DWithSubs = new SubtitlesDecorator(
                new ThreeDDecorator(
                        new BaseSession("Аватар: Шлях води", 150.0)
                )
        );
        printTicketInfo(session3DWithSubs);

        // Сценарій 3: Створити «тихий сеанс» зі знижкою вихідного дня -> вивести опис і ціну
        Session quietWeekendSession = new WeekendDiscountDecorator(
                new QuietSessionDecorator(
                        new BaseSession("Думками навиворіт 2", 140.0)
                )
        );
        printTicketInfo(quietWeekendSession);
    }
}
