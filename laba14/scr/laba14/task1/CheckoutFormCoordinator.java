package laba14.task1;

import java.util.HashMap;
import java.util.Map;

public class CheckoutFormCoordinator implements OrderFormMediator {
    private final Map<String, Runnable> eventHandlers = new HashMap<>();

    public void registerHandler(String event, Runnable action) {
        eventHandlers.put(event, action);
    }

    @Override
    public void notify(FormComponent sender, String event) {
        EventLogger.log("КоординаторФорми", "Отримано подію '" + event + "' від [" + sender.getName() + "]");

        Runnable handler = eventHandlers.get(event);
        if (handler != null) {
            handler.run();
        }
    }
}