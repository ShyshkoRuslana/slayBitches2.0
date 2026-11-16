package laba14.task1;

public class PromoCodeField extends FormComponent {
    private String promoCode = "";

    public PromoCodeField(OrderFormMediator mediator) {
        super(mediator, "ПолеПромокоду");
    }

    public void enterPromoCode(String code) {
        this.promoCode = code;
        EventLogger.log(name, "Введено промокод: " + code);
        mediator.notify(this, "PROMO_ENTERED");
    }

    public String getPromoCode() {
        return promoCode;
    }
}