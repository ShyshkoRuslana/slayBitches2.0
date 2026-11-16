package laba14.task1;

public class TicketCounter extends FormComponent {
    private int count = 0;

    public TicketCounter(OrderFormMediator mediator) {
        super(mediator, "ЛічильникКвитків");
    }

    public void updateCount(int count) {
        this.count = count;
        EventLogger.log(name, "Кількість квитків автоматично встановлено: " + this.count);
    }

    public int getCount() {
        return count;
    }
}
