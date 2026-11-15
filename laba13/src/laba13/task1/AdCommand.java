package laba13.task1;

public class AdCommand implements Command {
    private final CinemaHall hall;
    private final boolean playAds;
    private boolean previousState;

    public AdCommand(CinemaHall hall, boolean playAds) {
        this.hall = hall;
        this.playAds = playAds;
    }

    @Override
    public void execute() {
        previousState = hall.isAdsPlaying();
        EventLogger.log("Команда: AdCommand", "Передача запиту керування рекламою...");
        hall.setAdsPlaying(playAds);
    }

    @Override
    public void undo() {
        EventLogger.log("Команда: AdCommand (UNDO)", "Скасування дії з рекламою...");
        hall.setAdsPlaying(previousState);
    }

    @Override
    public String getDescription() {
        return playAds ? "Запустити рекламу" : "Зупинити рекламу";
    }
}
