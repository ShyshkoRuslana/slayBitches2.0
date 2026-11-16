package laba14.Task2;

import java.util.ArrayList;
import java.util.List;

// Типи подій, які можуть відбуватися із сеансом
enum EventType {
    PREMIERE_OPENED,
    RESCHEDULED,
    CANCELLED
}

// 1. Інтерфейс Спостерігача (Отримувача)
interface Observer {
    void update(String sessionTitle, EventType event, String details);
}

// 2. Видавець (Суб'єкт спостереження) — Сеанс
class Session {
    private final String title;
    private String dateTime;
    private final List<Observer> observers = new ArrayList<>();

    public Session(String title, String dateTime) {
        this.title = title;
        this.dateTime = dateTime;
    }

    public void subscribe(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers(EventType event, String details) {
        // Копіюємо список перед обходом, щоб уникнути ConcurrentModificationException,
        // якщо хтось із підписників вирішить відписатися прямо під час сповіщення
        for (Observer observer : new ArrayList<>(observers)) {
            observer.update(title, event, details);
        }
    }

    // Бізнес-методи сеансу
    public void openPremiereSales() {
        System.out.println("\n[ПОДІЯ] Відкрито продаж квитків на прем'єру «" + title + "» (" + dateTime + ")!");
        notifyObservers(EventType.PREMIERE_OPENED, "Дата: " + dateTime);
    }

    public void reschedule(String newDateTime) {
        String oldTime = this.dateTime;
        this.dateTime = newDateTime;
        System.out.println("\n[ПОДІЯ] Сеанс «" + title + "» перенесено з [" + oldTime + "] на [" + newDateTime + "]!");
        notifyObservers(EventType.RESCHEDULED, "Новий час: " + newDateTime);
    }

    public void cancel() {
        System.out.println("\n[ПОДІЯ] Сеанс «" + title + "» (" + dateTime + ") СКАСОВАНО!");
        notifyObservers(EventType.CANCELLED, "");
    }
}

// 3. Конкретні отримувачі (кожен сам вирішує, як реагувати)
class Website implements Observer {
    @Override
    public void update(String sessionTitle, EventType event, String details) {
        switch (event) {
            case PREMIERE_OPENED ->
                    System.out.println("  [Сайт] Активовано кнопку «Купити квиток» для «" + sessionTitle + "». " + details);
            case RESCHEDULED ->
                    System.out.println("  [Сайт] Оновлено розклад в афіші для «" + sessionTitle + "». " + details);
            case CANCELLED ->
                    System.out.println("  [Сайт] Сеанс «" + sessionTitle + "» позначено як скасований.");
        }
    }
}

class MobileApp implements Observer {
    @Override
    public void update(String sessionTitle, EventType event, String details) {
        switch (event) {
            case PREMIERE_OPENED ->
                    System.out.println("  [Мобільний застосунок] Push-розсилка: старт продажу на «" + sessionTitle + "»!");
            case RESCHEDULED ->
                    System.out.println("  [Мобільний застосунок] Оновлено дані в розділі «Мої квитки». " + details);
            case CANCELLED ->
                    System.out.println("  [Мобільний застосунок] Сеанс «" + sessionTitle + "» прибрано з активного розкладу.");
        }
    }
}

class Viewer implements Observer {
    private final String name;
    private boolean hasTicket;

    public Viewer(String name) {
        this.name = name;
        this.hasTicket = false;
    }

    public void buyTicket() {
        this.hasTicket = true;
        System.out.println("  * Глядач " + name + " купив(ла) квиток.");
    }

    @Override
    public void update(String sessionTitle, EventType event, String details) {
        // Глядач сам вирішує, чи реагувати на подію, залежно від наявності квитка
        if (event == EventType.PREMIERE_OPENED && !hasTicket) {
            System.out.println("  [Глядач " + name + "] Отримав сповіщення про старт продажу на «" + sessionTitle + "»!");
        } else if (event == EventType.RESCHEDULED && hasTicket) {
            System.out.println("  [Глядач " + name + " (має квиток)] Попереджений про перенесення «" + sessionTitle + "». " + details);
        } else if (event == EventType.CANCELLED && hasTicket) {
            System.out.println("  [Глядач " + name + " (має квиток)] Отримав сповіщення про скасування «" + sessionTitle + "». Оформлює повернення коштів.");
        }
    }
}

// --- ДЕМОНСТРАЦІЙНИЙ СЦЕНАРІЙ ---
public class Task2 {
    public static void main(String[] args) {
        // Створюємо сеанс та базові канали інформування
        Session premiere = new Session("Дюна: Месія", "15 грудня, 19:00");
        Website website = new Website();
        MobileApp app = new MobileApp();

        premiere.subscribe(website);
        premiere.subscribe(app);

        // Створюємо глядачів
        Viewer olena = new Viewer("Олена");
        Viewer andriy = new Viewer("Андрій");
        Viewer iryna = new Viewer("Ірина");

        // 1. Глядачі підписуються на прем'єру, відкривається продаж
        premiere.subscribe(olena);
        premiere.subscribe(andriy);
        premiere.subscribe(iryna);

        premiere.openPremiereSales();

        // Олена та Андрій купують квитки, а Ірина — ні
        System.out.println();
        olena.buyTicket();
        andriy.buyTicket();

        // 2. Переносимо сеанс -> сповіщення отримують глядачі з квитками (Олена і Андрій),
        // а Ірина (без квитка) ігнорує цю подію
        premiere.reschedule("16 грудня, 20:00");

        // 3. Андрій відписується, сеанс скасовується -> Андрій сповіщення не отримує
        System.out.println("\n* Андрій відписався від сповіщень сеансу.");
        premiere.unsubscribe(andriy);

        premiere.cancel();
    }
}