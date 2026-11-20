package laba16.task3;
import java.util.HashMap;
import java.util.Map;

public class Task3 {
    public static void main(String[] args) {
        HallEquipment hall = new HallEquipment();
        SettingsHistory history = new SettingsHistory();

        // 1. Налаштування під «бойовик»
        hall.setChannelVolume("Front", 80);
        hall.setChannelVolume("Center", 75);
        hall.setChannelVolume("Surround", 85);
        hall.setChannelVolume("Subwoofer", 95);
        hall.setEqualizerBand("Bass", +6);
        hall.setEqualizerBand("Mid", -2);
        hall.setEqualizerBand("Treble", +4);
        hall.setProjection(70, 85);

        history.savePreset("бойовик", hall.save());
        System.out.println("Збережено налаштування «бойовик»:\n" + hall + "\n");

        // 2. Зміна налаштувань під «мультфільм»
        hall.setChannelVolume("Front", 65);
        hall.setChannelVolume("Center", 80);
        hall.setChannelVolume("Surround", 60);
        hall.setChannelVolume("Subwoofer", 55);
        hall.setEqualizerBand("Bass", +1);
        hall.setEqualizerBand("Mid", +3);
        hall.setEqualizerBand("Treble", +2);
        hall.setProjection(90, 75);

        history.savePreset("мультфільм", hall.save());
        System.out.println("Збережено налаштування «мультфільм»:\n" + hall + "\n");

        // 3. Невдалий експеримент інженера
        hall.setChannelVolume("Subwoofer", 100);
        hall.setChannelVolume("Center", 20);
        hall.setEqualizerBand("Bass", +12);
        hall.setProjection(15, 100);
        System.out.println("Стан після невдалого експерименту:\n" + hall + "\n");

        // 4. Відновлення «мультфільм»
        hall.restore(history.getPreset("мультфільм"));
        System.out.println("Відновлено «мультфільм»:\n" + hall + "\n");

        // 5. Відновлення «бойовик»
        hall.restore(history.getPreset("бойовик"));
        System.out.println("Відновлено «бойовик»:\n" + hall);
    }
}

class HallEquipment {
    // Закритий внутрішній стан обладнання
    private Map<String, Integer> channelVolumes;
    private Map<String, Integer> equalizer;
    private int brightness;
    private int contrast;

    public HallEquipment() {
        this.channelVolumes = new HashMap<>();
        this.equalizer = new HashMap<>();

        setChannelVolume("Front", 50);
        setChannelVolume("Center", 50);
        setChannelVolume("Surround", 50);
        setChannelVolume("Subwoofer", 50);

        setEqualizerBand("Bass", 0);
        setEqualizerBand("Mid", 0);
        setEqualizerBand("Treble", 0);

        this.brightness = 50;
        this.contrast = 50;
    }

    public void setChannelVolume(String channel, int volume) {
        channelVolumes.put(channel, volume);
    }

    public void setEqualizerBand(String band, int level) {
        equalizer.put(band, level);
    }

    public void setProjection(int brightness, int contrast) {
        this.brightness = brightness;
        this.contrast = contrast;
    }

    // Створення незмінного знімка стану
    public Memento save() {
        return new Snapshot(channelVolumes, equalizer, brightness, contrast);
    }

    // Відновлення стану зі знімка
    public void restore(Memento memento) {
        if (!(memento instanceof Snapshot)) {
            throw new IllegalArgumentException("Некоректний об'єкт знімка налаштувань.");
        }
        Snapshot snapshot = (Snapshot) memento;
        this.channelVolumes = new HashMap<>(snapshot.channelVolumes);
        this.equalizer = new HashMap<>(snapshot.equalizer);
        this.brightness = snapshot.brightness;
        this.contrast = snapshot.contrast;
    }

    @Override
    public String toString() {
        return String.format(
                "  Канали звуку: %s%n  Еквалайзер:   %s%n  Проекція:     яскравість = %d%%, контраст = %d%%",
                channelVolumes, equalizer, brightness, contrast
        );
    }

    // «Вузький» інтерфейс для зовнішніх класів
    public interface Memento {
    }

    // Закрита реалізація знімка, недоступна навіть іншим класам у цьому ж файлі
    private static final class Snapshot implements Memento {
        private final Map<String, Integer> channelVolumes;
        private final Map<String, Integer> equalizer;
        private final int brightness;
        private final int contrast;

        private Snapshot(Map<String, Integer> channelVolumes,
                         Map<String, Integer> equalizer,
                         int brightness,
                         int contrast) {
            this.channelVolumes = Map.copyOf(channelVolumes);
            this.equalizer = Map.copyOf(equalizer);
            this.brightness = brightness;
            this.contrast = contrast;
        }
    }
}

class SettingsHistory {
    private final Map<String, HallEquipment.Memento> presets = new HashMap<>();

    public void savePreset(String name, HallEquipment.Memento memento) {
        if (name == null || name.isBlank() || memento == null) {
            throw new IllegalArgumentException("Назва пресету та знімок не можуть бути порожніми.");
        }
        presets.put(name.toLowerCase(), memento);
    }

    public HallEquipment.Memento getPreset(String name) {
        HallEquipment.Memento memento = presets.get(name.toLowerCase());
        if (memento == null) {
            throw new IllegalArgumentException("Пресет із назвою «" + name + "» не знайдено.");
        }
        return memento;
    }
}