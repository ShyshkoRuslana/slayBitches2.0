package laba13.task1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class HallOperatorPanel {
    private final Stack<Command> history = new Stack<>();
    private final List<String> auditLog = new ArrayList<>();

    public void executeCommand(Command command) {
        EventLogger.log("Пульт оператора", "Отримано запит: " + command.getDescription());
        command.execute();
        history.push(command);
        auditLog.add("ВИКОНАНО: " + command.getDescription());
    }

    public void undoLastCommand() {
        if (history.isEmpty()) {
            EventLogger.log("Пульт оператора", "Немає команд для скасування");
            return;
        }
        Command lastCommand = history.pop();
        EventLogger.log("Пульт оператора", "Отримано запит на скасування: " + lastCommand.getDescription());
        lastCommand.undo();
        auditLog.add("СКАСОВАНО: " + lastCommand.getDescription());
    }

    public void printCommandJournal() {
        System.out.println("\n=== ЖУРНАЛ ВИКОНАНИХ КОМАНД ОПЕРАТОРА ===");
        for (int i = 0; i < auditLog.size(); i++) {
            System.out.println((i + 1) + ". " + auditLog.get(i));
        }
        System.out.println("=========================================\n");
    }
}
