package laba14.task1;

public class Glasses3DToggle extends FormComponent {
    private boolean visible = false;
    private boolean checked = false;

    public Glasses3DToggle(OrderFormMediator mediator) {
        super(mediator, "Перемикач3DОкулярів");
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (!visible) {
            this.checked = false;
        }
        EventLogger.log(name, visible
                ? "Перемикач «3D-окуляри» ПОКАЗАНО"
                : "Перемикач «3D-окуляри» ПРИХОВАНО та скинуто");
    }

    public void toggle(boolean state) {
        if (visible) {
            this.checked = state;
            EventLogger.log(name, "Стан 3D-окулярів змінено на: " + (checked ? "Потрібні" : "Не потрібні"));
            mediator.notify(this, "GLASSES_TOGGLED");
        }
    }
}
