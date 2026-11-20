package laba16.task2;

import java.util.List;

public class Task2Runner {

    public static void runTask2() {
        System.out.println("=== ЗАВДАННЯ 2: ОПЕРАЦІЇ НАД ПОЗИЦІЯМИ ЗАМОВЛЕННЯ (ШАБЛОН VISITOR) ===\n");

        List<OrderItem> order = List.of(
                new TicketItem("Дюна: Частина друга (3D)", "Ряд 6, Місце 10", 240.0),
                new Glasses3DItem(1, 40.0),
                new PopcornItem("Сирний", "Великий (L)", 160.0),
                new DrinkItem("Кола", 500, 70.0),
                new GiftCertificateItem("KADR-2026-88", 500.0)
        );

        EventLogger.log("Замовлення", "Сформовано замовлення з " + order.size() + " позицій. Початковий стан:");
        for (OrderItem item : order) {
            EventLogger.log("Позиція", item.getTitle() + " — " + String.format("%.2f грн", item.getPrice()));
        }
        System.out.println();

        // 1. Розрахунок ПДВ
        EventLogger.log("Операція 1", "Запуск розрахунку ПДВ (VatCalculationVisitor) одним проходом:");
        VatCalculationVisitor vatVisitor = new VatCalculationVisitor();
        for (OrderItem item : order) {
            item.accept(vatVisitor);
        }
        EventLogger.log("Операція 1 [Результат]", String.format("Загальна сума ПДВ: %.2f грн", vatVisitor.getTotalVat()));
        System.out.println();

        // 2. Нарахування бонусів
        EventLogger.log("Операція 2", "Запуск нарахування бонусів (BonusPointsVisitor) одним проходом:");
        BonusPointsVisitor bonusVisitor = new BonusPointsVisitor();
        for (OrderItem item : order) {
            item.accept(bonusVisitor);
        }
        EventLogger.log("Операція 2 [Результат]", "Загалом нараховано бонусів: " + bonusVisitor.getTotalBonusPoints() + " балів");
        System.out.println();

        // 3. Додана операція «фіскальний чек» без зміни класів позицій
        EventLogger.log("Операція 3", "Запуск формування фіскального чека (FiscalReceiptVisitor):");
        FiscalReceiptVisitor receiptVisitor = new FiscalReceiptVisitor();
        for (OrderItem item : order) {
            item.accept(receiptVisitor);
        }
        receiptVisitor.printReceipt();
        System.out.println();

        EventLogger.log("Завершення", "Стан усіх позицій замовлення після 3 операцій залишився незмінним.");
    }

    public static void main(String[] args) {
        runTask2();
    }
}
