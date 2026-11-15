package laba13.task1;


public class PauseCommand implements Command {
    private final CinemaHall hall;
    private final boolean pauseState;
    private boolean previousState;

    public PauseCommand(CinemaHall hall, boolean pauseState) {
        this.hall = hall;
        this.pauseState = pauseState;
    }

    @Override
    public void execute() {
        previousState = hall.isPaused();
        EventLogger.log("Команда: PauseCommand", "Передача запиту на зміну стану відтворення...");
        hall.setPaused(pauseState);
    }

    @Override
    public void undo() {
        EventLogger.log("Команда: PauseCommand (UNDO)", "Повернення попереднього стану відтворення...");
        hall.setPaused(previousState);
    }

    @Override
    public String getDescription() {
        return pauseState ? "Поставити показ на паузу" : "Зняти показ з паузи";
    }
}
