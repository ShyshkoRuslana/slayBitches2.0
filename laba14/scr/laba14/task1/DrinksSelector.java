package laba14.task1;

public class DrinksSelector extends FormComponent {
    private String selectedDrink = "Без напоїв";

    public DrinksSelector(OrderFormMediator mediator) {
        super(mediator, "ВибірНапоїв");
    }

    public void selectDrink(String drink) {
        this.selectedDrink = drink;
        EventLogger.log(name, "Додано до замовлення напій: " + drink);
        mediator.notify(this, "DRINK_SELECTED");
    }

    public String getSelectedDrink() {
        return selectedDrink;
    }
}