package laba13.task1;

public class LightCommand implements Command {
    private final CinemaHall hall;
    private final boolean targetState;
    private boolean previousState;

    public LightCommand(CinemaHall hall, boolean targetState) {
        this.hall = hall;
        this.targetState = targetState;
    }

    @Override
    public void execute() {
        previousState = hall.isLightsOn();
        EventLogger.log("Команда: LightCommand", "Передача запиту на зміну освітлення...");
        hall.setLights(targetState);
    }

    @Override
    public void undo() {
        EventLogger.log("Команда: LightCommand (UNDO)", "Скасування зміни освітлення...");
        hall.setLights(previousState);
    }

    @Override
    public String getDescription() {
        return targetState ? "Увімкнути світло" : "Вимкнути світло";
    }
}
