package laba13.task1.Task2;

import java.util.ArrayList;
import java.util.List;
/*
Складна умовна логіка
Ланцюжок обов'язків)*/
// 1. Клас замовлення
class Order {
    private double originalPrice;
    private double currentPrice;
    private boolean hasPromoCode;
    private boolean isStudent;
    private boolean isBirthday;
    private boolean isPensioner;
    private List<String> appliedDiscounts = new ArrayList<>();

    public Order(double originalPrice, boolean hasPromoCode, boolean isStudent, boolean isBirthday, boolean isPensioner) {
        this.originalPrice = originalPrice;
        this.currentPrice = originalPrice;
        this.hasPromoCode = hasPromoCode;
        this.isStudent = isStudent;
        this.isBirthday = isBirthday;
        this.isPensioner = isPensioner;
    }

    public double getOriginalPrice() { return originalPrice; }
    public double getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public boolean hasPromoCode() { return hasPromoCode; }
    public boolean isStudent() { return isStudent; }
    public boolean isBirthday() { return isBirthday; }
    public boolean isPensioner() { return isPensioner; }

    public void addDiscountLog(String discountName) {
        appliedDiscounts.add(discountName);
    }

    public void printResult() {
        System.out.println("Початкова ціна: " + originalPrice + " грн");
        System.out.println("Кінцева ціна: " + currentPrice + " грн");
        System.out.println("Застосовані знижки: " + (appliedDiscounts.isEmpty() ? "Немає" : String.join(", ", appliedDiscounts)));
        System.out.println("--------------------------------------------------");
    }
}

// 2. Інтерфейс обробника (Handler)
interface DiscountHandler {
    void setNext(DiscountHandler nextHandler);
    void handle(Order order);
}

// 3. Конкретний обробник: Промокод (зупиняє подальшу обробку, якщо спрацював)
class PromoCodeHandler implements DiscountHandler {
    private DiscountHandler nextHandler;

    @Override
    public void setNext(DiscountHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handle(Order order) {
        if (order.hasPromoCode()) {
            // Промокод дає знижку 30% і повністю зупиняє ланцюжок
            double newPrice = order.getCurrentPrice() * 0.7;
            order.setCurrentPrice(newPrice);
            order.addDiscountLog("Знижка за промокодом (30%)");
            System.out.println("-> Промокод застосовано. Подальші правила заблоковано.");
            return;
        }

        // Якщо промокоду немає, передаємо замовлення далі
        if (nextHandler != null) {
            nextHandler.handle(order);
        }
    }
}

// 4. Конкретний обробник: Студентська знижка
class StudentDiscountHandler implements DiscountHandler {
    private DiscountHandler nextHandler;

    @Override
    public void setNext(DiscountHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handle(Order order) {
        if (order.isStudent()) {
            // Студентська знижка 15%
            double newPrice = order.getCurrentPrice() * 0.85;
            order.setCurrentPrice(newPrice);
            order.addDiscountLog("Студентська знижка (15%)");
            System.out.println("-> Студентську знижку застосовано.");
        }

        // Передаємо далі по ланцюжку (може поєднуватися з іншими, наприклад, з днем народження)
        if (nextHandler != null) {
            nextHandler.handle(order);
        }
    }
}

// 5. Нове правило: Знижка для пенсіонерів
class PensionerDiscountHandler implements DiscountHandler {
    private DiscountHandler nextHandler;

    @Override
    public void setNext(DiscountHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handle(Order order) {
        if (order.isPensioner()) {
            // Знижка для пенсіонерів 20%
            double newPrice = order.getCurrentPrice() * 0.8;
            order.setCurrentPrice(newPrice);
            order.addDiscountLog("Знижка для пенсіонерів (20%)");
            System.out.println("-> Знижку для пенсіонерів застосовано.");
        }

        if (nextHandler != null) {
            nextHandler.handle(order);
        }
    }
}

// 6. Демонстраційний клас
public class Task2 {
    public static void main(String[] args) {
        // Налаштування ланцюжка: Промокод -> Студентська знижка -> Пенсійна знижка
        DiscountHandler promoHandler = new PromoCodeHandler();
        DiscountHandler studentHandler = new StudentDiscountHandler();
        DiscountHandler pensionerHandler = new PensionerDiscountHandler();

        // Зв'язуємо ланцюжок: Promo -> Student -> Pensioner
        promoHandler.setNext(studentHandler);
        studentHandler.setNext(pensionerHandler);

        System.out.println("=== Сценарій 1: Замовлення студента без промокоду ===");
        Order order1 = new Order(200.0, false, true, false, false);
        promoHandler.handle(order1);
        order1.printResult();

        System.out.println("=== Сценарій 2: Замовлення з промокодом (та студента) ===");
        Order order2 = new Order(200.0, true, true, false, false);
        promoHandler.handle(order2);
        order2.printResult();

        System.out.println("=== Сценарій 3: Додавання пенсіонера в ланцюг ===");
        Order order3 = new Order(200.0, false, false, false, true);
        promoHandler.handle(order3);
        order3.printResult();

        System.out.println("=== Сценарій 4: Додавання пенсіонера + студент  ===");
        Order order4 = new Order(200.0, false, true, false, true);
        promoHandler.handle(order4);
        order4.printResult();
    }
}