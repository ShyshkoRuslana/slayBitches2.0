package laba14.task1;

public abstract class FormComponent {
    protected OrderFormMediator mediator;
    protected final String name;

    public FormComponent(OrderFormMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
