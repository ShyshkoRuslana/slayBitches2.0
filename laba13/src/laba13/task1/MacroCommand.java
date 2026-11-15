package laba13.task1;

import java.util.List;

public class MacroCommand implements Command {
    private final String macroName;
    private final List<Command> commands;

    public MacroCommand(String macroName, List<Command> commands) {
        this.macroName = macroName;
        this.commands = commands;
    }

    @Override
    public void execute() {
        EventLogger.log("Макрокоманда: " + macroName, "Початок виконання набору з " + commands.size() + " команд");
        for (Command cmd : commands) {
            cmd.execute();
        }
        EventLogger.log("Макрокоманда: " + macroName, "Виконання набору завершено");
    }

    @Override
    public void undo() {
        EventLogger.log("Макрокоманда: " + macroName + " (UNDO)", "Скасування набору команд у зворотному порядку");
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }

    @Override
    public String getDescription() {
        return "Набір команд «" + macroName + "»";
    }
}
