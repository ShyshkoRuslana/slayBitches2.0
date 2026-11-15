package laba13.task1;

public class CinemaHall {
    private final String hallName;
    private boolean lightsOn = true;
    private int volume = 50;
    private boolean isPaused = true;
    private boolean adsPlaying = false;

    public CinemaHall(String hallName) {
        this.hallName = hallName;
    }

    public void setLights(boolean on) {
        this.lightsOn = on;
        EventLogger.log("Обладнання (" + hallName + ")", "Світло в залі " + (on ? "УВІМКНЕНО" : "ВИМКНЕНО"));
    }

    public boolean isLightsOn() {
        return lightsOn;
    }

    public void setVolume(int newVolume) {
        this.volume = Math.max(0, Math.min(100, newVolume));
        EventLogger.log("Аудіосистема (" + hallName + ")", "Гучність встановлено на " + this.volume + "%");
    }

    public int getVolume() {
        return volume;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
        EventLogger.log("Проєктор (" + hallName + ")", paused ? "Показ поставлено на ПАУЗУ" : "Показ ВІДНОВЛЕНО");
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setAdsPlaying(boolean playing) {
        this.adsPlaying = playing;
        EventLogger.log("Медіасервер (" + hallName + ")", playing ? "Запущено рекламний блок" : "Рекламний блок зупинено");
    }

    public boolean isAdsPlaying() {
        return adsPlaying;
    }
}
