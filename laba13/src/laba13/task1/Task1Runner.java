package laba13.task1;

import java.util.List;

public class Task1Runner {
    public static void main(String[] args) {
        CinemaHall hall1 = new CinemaHall("Зала №1 (IMAX)");
        HallOperatorPanel panel = new HallOperatorPanel();

        System.out.println("--- Крок 1: Зміна гучності та її скасування ---");
        panel.executeCommand(new VolumeCommand(hall1, 0));
        panel.undoLastCommand();

        System.out.println("\n--- Крок 2: Виконання набору «Початок сеансу» ---");
        List<Command> startSessionCommands = List.of(
                new LightCommand(hall1, false),
                new VolumeCommand(hall1, 75),
                new AdCommand(hall1, true),
                new PauseCommand(hall1, false)
        );
        Command startSessionMacro = new MacroCommand("Початок сеансу", startSessionCommands);
        panel.executeCommand(startSessionMacro);

        System.out.println("\n--- Крок 3: Виконання набору «Аварійна ситуація» ---");
        List<Command> emergencyCommands = List.of(
                new PauseCommand(hall1, true),
                new AdCommand(hall1, false),
                new VolumeCommand(hall1, 10),
                new LightCommand(hall1, true)
        );
        Command emergencyMacro = new MacroCommand("Аварійна ситуація", emergencyCommands);
        panel.executeCommand(emergencyMacro);

        System.out.println("\n--- Крок 4: Вивід журналу команд ---");
        panel.printCommandJournal();
    }
}
