package laba15.task2;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 1. Інтерфейс Стану (State)
interface OrderState {
    String getStateName();
    void changeSeats(TicketOrder order, List<String> newSeats);
    void reserve(TicketOrder order);
    void pay(TicketOrder order);
    void expireReservation(TicketOrder order); // Тайм-аут 15 хв
    void enterHall(TicketOrder order);
    void refund(TicketOrder order);
}

// 2. Конкретні Стани (Concrete States)

// Стан 1: Кошик
class CartState implements OrderState {
    @Override
    public String getStateName() {
        return "КОШИК";
    }

    @Override
    public void changeSeats(TicketOrder order, List<String> newSeats) {
        order.setSeatsInternal(newSeats);
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "Оновлено вибрані місця: " + newSeats);
    }

    @Override
    public void reserve(TicketOrder order) {
        if (order.getSeats().isEmpty()) {
            EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                    "ВІДМОВА: неможливо забронювати порожній кошик.");
            return;
        }
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "Місця " + order.getSeats() + " заброньовано на 15 хвилин.");
        order.setState(new ReservedState());
    }

    @Override
    public void pay(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: спочатку потрібно забронювати місця перед оплатою.");
    }

    @Override
    public void expireReservation(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: замовлення ще не заброньовано (таймер 15 хв не запущено).");
    }

    @Override
    public void enterHall(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА у вході в зал: замовлення не оплачене (перебуває в кошику).");
    }

    @Override
    public void refund(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: замовлення ще не оплачене, повертати нічого.");
    }
}

// Стан 2: Місця заброньовано (на 15 хвилин)
class ReservedState implements OrderState {
    @Override
    public String getStateName() {
        return "МІСЦЯ ЗАБРОНЬОВАНО";
    }

    @Override
    public void changeSeats(TicketOrder order, List<String> newSeats) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: змінити місця можна лише у стані «Кошик».");
    }

    @Override
    public void reserve(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: місця вже заброньовано.");
    }

    @Override
    public void pay(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "Оплата пройшла успішно! Квитки сформовано.");
        order.setState(new PaidState());
    }

    @Override
    public void expireReservation(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "Минуло 15 хвилин без оплати — бронювання автоматично скасовано, місця повернуто у продаж.");
        order.setState(new CancelledState());
    }

    @Override
    public void enterHall(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА у вході в зал: бронювання не оплачене.");
    }

    @Override
    public void refund(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: кошти не списувалися (замовлення лише заброньоване).");
    }
}

// Стан 3: Оплачено
class PaidState implements OrderState {
    @Override
    public String getStateName() {
        return "ОПЛАЧЕНО";
    }

    @Override
    public void changeSeats(TicketOrder order, List<String> newSeats) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: неможливо змінити місця в оплаченому замовленні.");
    }

    @Override
    public void reserve(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: замовлення вже оплачене.");
    }

    @Override
    public void pay(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: замовлення вже оплачене повторно.");
    }

    @Override
    public void expireReservation(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ІГНОРУВАННЯ: замовлення вже оплачене, таймер скасування не діє.");
    }

    @Override
    public void enterHall(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "Контролер просканував квиток. Прохід у зал дозволено!");
        order.setState(new UsedState());
    }

    @Override
    public void refund(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "Кошти повернуто клієнту до початку сеансу. Замовлення скасовано.");
        order.setState(new CancelledState());
    }
}

// Стан 4: Використано
class UsedState implements OrderState {
    @Override
    public String getStateName() {
        return "ВИКОРИСТАНО";
    }

    @Override
    public void changeSeats(TicketOrder order, List<String> newSeats) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: квиток вже використано для входу в зал.");
    }

    @Override
    public void reserve(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: замовлення вже завершено.");
    }

    @Override
    public void pay(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: замовлення вже використано.");
    }

    @Override
    public void expireReservation(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: операція недоступна для використаного квитка.");
    }

    @Override
    public void enterHall(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: повторний вхід за вже використаним квитком заборонено.");
    }

    @Override
    public void refund(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: неможливо повернути гроші після того, як глядач пройшов у зал.");
    }
}

// Стан 5: Скасовано
class CancelledState implements OrderState {
    @Override
    public String getStateName() {
        return "СКАСОВАНО";
    }

    @Override
    public void changeSeats(TicketOrder order, List<String> newSeats) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: замовлення скасовано, редагування неможливе.");
    }

    @Override
    public void reserve(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: замовлення скасовано. Створіть нове замовлення.");
    }

    @Override
    public void pay(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: неможливо оплатити скасоване замовлення.");
    }

    @Override
    public void expireReservation(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "Замовлення вже перебуває у стані «Скасовано».");
    }

    @Override
    public void enterHall(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА у вході в зал: замовлення скасовано.");
    }

    @Override
    public void refund(TicketOrder order) {
        EventLogger.log("Order #" + order.getId() + " [" + getStateName() + "]",
                "ВІДМОВА: замовлення вже скасовано.");
    }
}

// 3. Допоміжний клас для логування у форматі [час] [джерело] подія
class EventLogger {
    private static int minutesOffset = 0;

    public static void log(String source, String event) {
        LocalTime time = LocalTime.of(18, 0).plusMinutes(minutesOffset);
        minutesOffset += 3;
        String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        System.out.printf("[%s] [%s] %s%n", formattedTime, source, event);
    }
}

// 4. Контекст (Context) — Замовлення квитків
class TicketOrder {
    private final String id;
    private final String movieTitle;
    private List<String> seats;
    private OrderState state;

    public TicketOrder(String id, String movieTitle, List<String> initialSeats) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.seats = new ArrayList<>(initialSeats);
        this.state = new CartState();
        EventLogger.log("TicketOrder #" + id,
                "Створено замовлення на фільм «" + movieTitle + "», місця: " + seats + ". Початковий стан: [" + state.getStateName() + "]");
    }

    public void setState(OrderState newState) {
        String oldStateName = this.state.getStateName();
        this.state = newState;
        EventLogger.log("TicketOrder #" + id,
                "ПЕРЕХІД СТАНУ: [" + oldStateName + "] -> [" + newState.getStateName() + "]");
    }

    public String getId() {
        return id;
    }

    public List<String> getSeats() {
        return seats;
    }

    void setSeatsInternal(List<String> newSeats) {
        this.seats = new ArrayList<>(newSeats);
    }

    // Делегування усіх дій поточному об'єкту стану (жодних if/switch!)
    public void changeSeats(List<String> newSeats) {
        state.changeSeats(this, newSeats);
    }

    public void reserve() {
        state.reserve(this);
    }

    public void pay() {
        state.pay(this);
    }

    public void expireReservation() {
        state.expireReservation(this);
    }

    public void enterHall() {
        state.enterHall(this);
    }

    public void refund() {
        state.refund(this);
    }
}

// 5. Клас-раннер для відтворення демонстраційного сценарію
public class task2 {

    public static void runTask2() {
        System.out.println("=== ЗАВДАННЯ 2: СТАН ЗАМОВЛЕННЯ (ШАБЛОН STATE) ===");

        // Сценарій 1: Провести замовлення від кошика до використання
        TicketOrder order1 = new TicketOrder("101", "Інтерстеллар", Arrays.asList("Ряд 5, Місце 7", "Ряд 5, Місце 8"));
        order1.changeSeats(Arrays.asList("Ряд 6, Місце 10", "Ряд 6, Місце 11")); // Дозволено в кошику
        order1.reserve();   // Кошик -> Місця заброньовано
        order1.pay();       // Місця заброньовано -> Оплачено
        order1.enterHall(); // Оплачено -> Використано
        order1.refund();    // Спроба повернути гроші після використання -> Відмова

        System.out.println("--------------------------------------------------");

        // Сценарій 2 та 3: Неоплачене бронювання, спроба входу в зал -> відмова, тайм-аут 15 хв -> автоматичне скасування
        TicketOrder order2 = new TicketOrder("102", "Оппенгеймер", List.of("Ряд 3, Місце 4"));
        order2.reserve();           // Кошик -> Місця заброньовано
        order2.enterHall();         // Спроба пройти в зал з неоплаченим замовленням -> Відмова
        order2.expireReservation(); // Минуло 15 хвилин -> Автоматичне скасування
        order2.pay();               // Спроба оплатити після скасування -> Відмова
    }

    public static void main(String[] args) {
        runTask2();
    }
}
