package laba14.task1;

public class PayButton extends FormComponent {
    private boolean enabled = false;

    public PayButton(OrderFormMediator mediator) {
        super(mediator, "КнопкаОплати");
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        EventLogger.log(name, enabled ? "Кнопка «Оплатити» АКТИВНА" : "Кнопка «Оплатити» ДЕАКТИВОВАНА");
    }

    public void click() {
        if (enabled) {
            EventLogger.log(name, "Натиснуто оплату замовлення!");
            mediator.notify(this, "PAY_CLICKED");
        } else {
            EventLogger.log(name, "Спроба натиснути неактивну кнопку оплати відхилена");
        }
    }
}
