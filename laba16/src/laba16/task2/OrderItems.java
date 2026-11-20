package laba16.task2;

interface OrderItem {
    void accept(OrderItemVisitor visitor);
    String getTitle();
    double getPrice();
}

class TicketItem implements OrderItem {
    private final String movieTitle;
    private final String seat;
    private final double price;

    TicketItem(String movieTitle, String seat, double price) {
        this.movieTitle = movieTitle;
        this.seat = seat;
        this.price = price;
    }

    @Override
    public void accept(OrderItemVisitor visitor) { visitor.visit(this); }

    @Override
    public String getTitle() { return "Квиток на «" + movieTitle + "» (" + seat + ")"; }

    @Override
    public double getPrice() { return price; }
}

class DrinkItem implements OrderItem {
    private final String name;
    private final int volumeMl;
    private final double price;

    DrinkItem(String name, int volumeMl, double price) {
        this.name = name;
        this.volumeMl = volumeMl;
        this.price = price;
    }

    @Override
    public void accept(OrderItemVisitor visitor) { visitor.visit(this); }

    @Override
    public String getTitle() { return "Напій «" + name + "» (" + volumeMl + " мл)"; }

    @Override
    public double getPrice() { return price; }
}

class PopcornItem implements OrderItem {
    private final String flavor;
    private final String size;
    private final double price;

    PopcornItem(String flavor, String size, double price) {
        this.flavor = flavor;
        this.size = size;
        this.price = price;
    }

    @Override
    public void accept(OrderItemVisitor visitor) { visitor.visit(this); }

    @Override
    public String getTitle() { return "Попкорн " + flavor + " (" + size + ")"; }

    @Override
    public double getPrice() { return price; }
}

class Glasses3DItem implements OrderItem {
    private final int quantity;
    private final double pricePerUnit;

    Glasses3DItem(int quantity, double pricePerUnit) {
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public void accept(OrderItemVisitor visitor) { visitor.visit(this); }

    @Override
    public String getTitle() { return "3D-окуляри (" + quantity + " шт.)"; }

    @Override
    public double getPrice() { return quantity * pricePerUnit; }
}

class GiftCertificateItem implements OrderItem {
    private final String code;
    private final double nominalValue;

    GiftCertificateItem(String code, double nominalValue) {
        this.code = code;
        this.nominalValue = nominalValue;
    }

    @Override
    public void accept(OrderItemVisitor visitor) { visitor.visit(this); }

    @Override
    public String getTitle() { return "Подарунковий сертифікат #" + code; }

    @Override
    public double getPrice() { return nominalValue; }
}
