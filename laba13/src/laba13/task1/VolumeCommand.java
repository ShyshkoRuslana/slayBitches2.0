package laba13.task1;

public class VolumeCommand implements Command {
    private final CinemaHall hall;
    private final int newVolume;
    private int previousVolume;

    public VolumeCommand(CinemaHall hall, int newVolume) {
        this.hall = hall;
        this.newVolume = newVolume;
    }

    @Override
    public void execute() {
        previousVolume = hall.getVolume();
        EventLogger.log("Команда: VolumeCommand", "Передача запиту на зміну гучності (" + previousVolume + "% -> " + newVolume + "%)");
        hall.setVolume(newVolume);
    }

    @Override
    public void undo() {
        EventLogger.log("Команда: VolumeCommand (UNDO)", "Повернення попередньої гучності (" + previousVolume + "%)");
        hall.setVolume(previousVolume);
    }

    @Override
    public String getDescription() {
        return "Змінити гучність на " + newVolume + "%";
    }
}
