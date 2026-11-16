package laba14.task1;

import java.util.ArrayList;
import java.util.List;

public class SeatMap extends FormComponent {
    private final List<String> selectedSeats = new ArrayList<>();

    public SeatMap(OrderFormMediator mediator) {
        super(mediator, "СхемаМісць");
    }

    public void selectSeats(List<String> seats) {
        selectedSeats.clear();
        selectedSeats.addAll(seats);
        EventLogger.log(name, "Користувач обрав місця: " + selectedSeats);
        mediator.notify(this, "SEATS_SELECTED");
    }

    public void resetSeats() {
        selectedSeats.clear();
        EventLogger.log(name, "Схема місць оновлена, вибрані раніше місця скинуто");
    }

    public int getSelectedCount() {
        return selectedSeats.size();
    }
}
