package laba14.task1;

import java.util.List;

public class Task1 {

    public static void runScenario() {
        CheckoutFormCoordinator coordinator = new CheckoutFormCoordinator();

        SessionSelector sessionSelector = new SessionSelector(coordinator);
        SeatMap seatMap = new SeatMap(coordinator);
        TicketCounter ticketCounter = new TicketCounter(coordinator);
        Glasses3DToggle glassesToggle = new Glasses3DToggle(coordinator);
        PromoCodeField promoCodeField = new PromoCodeField(coordinator);
        PayButton payButton = new PayButton(coordinator);

        // Реєструємо правила взаємодії без редагування самого координатора
        coordinator.registerHandler("SESSION_CHANGED", () -> {
            seatMap.resetSeats();
            ticketCounter.updateCount(0);
            payButton.setEnabled(false);

            boolean is3D = "3D".equalsIgnoreCase(sessionSelector.getSessionType());
            glassesToggle.setVisible(is3D);
        });

        coordinator.registerHandler("SEATS_SELECTED", () -> {
            int count = seatMap.getSelectedCount();
            ticketCounter.updateCount(count);
            payButton.setEnabled(count > 0);
        });

        coordinator.registerHandler("PROMO_ENTERED", () ->
                EventLogger.log("КоординаторФорми", "Застосовано знижку за промокодом «" + promoCodeField.getPromoCode() + "»")
        );

        coordinator.registerHandler("PAY_CLICKED", () ->
                EventLogger.log("КоординаторФорми", "Перенаправлення на платіжний шлюз...")
        );

        System.out.println("--- Крок 1: Вибір 3D-сеансу ---");
        sessionSelector.selectSession("Аватар: Шлях води", "3D");

        System.out.println("\n--- Крок 2: Вибір 2 місць ---");
        seatMap.selectSeats(List.of("Ряд 5, Місце 10", "Ряд 5, Місце 11"));
        glassesToggle.toggle(true);

        System.out.println("\n--- Крок 3: Зміна сеансу на 2D ---");
        sessionSelector.selectSession("Дюна: Частина друга", "2D");

        System.out.println("\n--- Крок 4: Додавання нового елемента (Напої) без змін у координаторі ---");
        DrinksSelector drinksSelector = new DrinksSelector(coordinator);
        coordinator.registerHandler("DRINK_SELECTED", () ->
                EventLogger.log("КоординаторФорми", "Оновлено чек: додано напій «" + drinksSelector.getSelectedDrink() + "»")
        );

        seatMap.selectSeats(List.of("Ряд 7, Місце 4"));
        promoCodeField.enterPromoCode("KADR2026");
        drinksSelector.selectDrink("Кола 0.5л");
        payButton.click();
    }

    public static void main(String[] args) {
        runScenario();
    }
}