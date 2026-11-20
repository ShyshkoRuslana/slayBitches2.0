package laba16.task2;

import java.util.ArrayList;
import java.util.List;

interface OrderItemVisitor {
    void visit(TicketItem ticket);
    void visit(DrinkItem drink);
    void visit(PopcornItem popcorn);
    void visit(Glasses3DItem glasses);
    void visit(GiftCertificateItem certificate);
}

class VatCalculationVisitor implements OrderItemVisitor {
    private double totalVat = 0.0;

    @Override
    public void visit(TicketItem ticket) {
        double vat = ticket.getPrice() * 0.07; // 7% пільговий ПДВ на кінопокази
        totalVat += vat;
        EventLogger.log("VatVisitor", String.format("%s -> ціна %.2f грн, ставка 7%%, ПДВ = %.2f грн",
                ticket.getTitle(), ticket.getPrice(), vat));
    }

    @Override
    public void visit(DrinkItem drink) {
        double vat = drink.getPrice() * 0.20;
        totalVat += vat;
        EventLogger.log("VatVisitor", String.format("%s -> ціна %.2f грн, ставка 20%%, ПДВ = %.2f грн",
                drink.getTitle(), drink.getPrice(), vat));
    }

    @Override
    public void visit(PopcornItem popcorn) {
        double vat = popcorn.getPrice() * 0.20;
        totalVat += vat;
        EventLogger.log("VatVisitor", String.format("%s -> ціна %.2f грн, ставка 20%%, ПДВ = %.2f грн",
                popcorn.getTitle(), popcorn.getPrice(), vat));
    }

    @Override
    public void visit(Glasses3DItem glasses) {
        double vat = glasses.getPrice() * 0.20;
        totalVat += vat;
        EventLogger.log("VatVisitor", String.format("%s -> ціна %.2f грн, ставка 20%%, ПДВ = %.2f грн",
                glasses.getTitle(), glasses.getPrice(), vat));
    }

    @Override
    public void visit(GiftCertificateItem certificate) {
        EventLogger.log("VatVisitor", String.format("%s -> ціна %.2f грн, ставка 0%%, ПДВ = 0.00 грн",
                certificate.getTitle(), certificate.getPrice()));
    }

    double getTotalVat() { return totalVat; }
}

class BonusPointsVisitor implements OrderItemVisitor {
    private int totalBonusPoints = 0;

    @Override
    public void visit(TicketItem ticket) {
        int points = (int) (ticket.getPrice() * 0.10);
        totalBonusPoints += points;
        EventLogger.log("BonusVisitor", ticket.getTitle() + " -> нараховано " + points + " бонусів (10%)");
    }

    @Override
    public void visit(DrinkItem drink) {
        int points = (int) (drink.getPrice() * 0.05);
        totalBonusPoints += points;
        EventLogger.log("BonusVisitor", drink.getTitle() + " -> нараховано " + points + " бонусів (5%)");
    }

    @Override
    public void visit(PopcornItem popcorn) {
        int points = (int) (popcorn.getPrice() * 0.15);
        totalBonusPoints += points;
        EventLogger.log("BonusVisitor", popcorn.getTitle() + " -> нараховано " + points + " бонусів (15%)");
    }

    @Override
    public void visit(Glasses3DItem glasses) {
        int points = (int) (glasses.getPrice() * 0.05);
        totalBonusPoints += points;
        EventLogger.log("BonusVisitor", glasses.getTitle() + " -> нараховано " + points + " бонусів (5%)");
    }

    @Override
    public void visit(GiftCertificateItem certificate) {
        EventLogger.log("BonusVisitor", certificate.getTitle() + " -> 0 бонусів");
    }

    int getTotalBonusPoints() { return totalBonusPoints; }
}

class FiscalReceiptVisitor implements OrderItemVisitor {
    private final List<String> receiptLines = new ArrayList<>();
    private double totalSum = 0.0;

    @Override
    public void visit(TicketItem ticket) { addLine("[ПОСЛУГА КІНОПОКАЗУ]", ticket.getTitle(), ticket.getPrice()); }

    @Override
    public void visit(DrinkItem drink) { addLine("[БАР / НАПОЇ]", drink.getTitle(), drink.getPrice()); }

    @Override
    public void visit(PopcornItem popcorn) { addLine("[БАР / СНЕКИ]", popcorn.getTitle(), popcorn.getPrice()); }

    @Override
    public void visit(Glasses3DItem glasses) { addLine("[АКСЕСУАРИ]", glasses.getTitle(), glasses.getPrice()); }

    @Override
    public void visit(GiftCertificateItem certificate) { addLine("[СЕРТИФІКАТ]", certificate.getTitle(), certificate.getPrice()); }

    private void addLine(String category, String title, double price) {
        receiptLines.add(String.format("%-22s %-42s %8.2f грн", category, title, price));
        totalSum += price;
        EventLogger.log("FiscalReceiptVisitor", "Додано рядок чека: " + title + " (" + price + " грн)");
    }

    void printReceipt() {
        System.out.println("----------------------------------------------------------------------------");
        System.out.println("                    ФІСКАЛЬНИЙ ЧЕК — КІНОТЕАТР «КАДР»");
        System.out.println("----------------------------------------------------------------------------");
        for (String line : receiptLines) {
            System.out.println(line);
        }
        System.out.println("----------------------------------------------------------------------------");
        System.out.printf("РАЗОМ ДО СПЛАТИ: %57.2f грн%n", totalSum);
        System.out.println("----------------------------------------------------------------------------");
    }
}
