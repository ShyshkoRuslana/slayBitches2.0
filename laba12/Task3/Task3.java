package laba12.Task3;

public class Task3 {

    // =========================================================
    // 1. ІЄРАРХІЯ РЕАЛІЗАЦІЇ (Носії — ДЕ показується реклама)
    // =========================================================

    // Інтерфейс реалізації ("Міст" веде сюди)
    interface DisplayMedium {
        void renderAd(String adType, String content, int durationSeconds);
    }

    // Конкретна реалізація 1: Екран кінозали
    static class HallScreen implements DisplayMedium {
        @Override
        public void renderAd(String adType, String content, int durationSeconds) {
            System.out.println("[Екран зали | 4K + Dolby Atmos] (" + durationSeconds + " сек) "
                    + adType + ": " + content);
        }
    }

    // Конкретна реалізація 2: Екрани у фоє
    static class LobbyScreens implements DisplayMedium {
        @Override
        public void renderAd(String adType, String content, int durationSeconds) {
            System.out.println("[Екрани у фоє | Без звуку, повтор] (" + durationSeconds + " сек) "
                    + adType + ": " + content);
        }
    }

    // Конкретна реалізація 3: Мобільний застосунок
    static class MobileApp implements DisplayMedium {
        @Override
        public void renderAd(String adType, String content, int durationSeconds) {
            System.out.println("[Мобільний застосунок | Відео-банер + кнопка] (" + durationSeconds + " сек) "
                    + adType + ": " + content);
        }
    }

    // Новий носій, доданий ОДНИМ класом без зміни існуючих класів реклами
    static class LedFacade implements DisplayMedium {
        @Override
        public void renderAd(String adType, String content, int durationSeconds) {
            System.out.println("[Світлодіодний фасад | Вулична яскравість] (" + durationSeconds + " сек) "
                    + adType + ": " + content);
        }
    }

    // =========================================================
    // 2. ІЄРАРХІЯ АБСТРАКЦІЇ (Види реклами — ЩО показується)
    // =========================================================

    // Базова абстракція
    static abstract class Advertisement {
        // Посилання на носій — це і є "Міст" між двома ієрархіями
        protected DisplayMedium medium;

        public Advertisement(DisplayMedium medium) {
            if (medium == null) {
                throw new IllegalArgumentException("Носій для показу реклами не може бути null.");
            }
            this.medium = medium;
        }

        // Можливість змінити носій для тієї самої реклами прямо під час роботи програми
        public void setMedium(DisplayMedium medium) {
            if (medium == null) {
                throw new IllegalArgumentException("Носій для показу реклами не може бути null.");
            }
            this.medium = medium;
        }

        public abstract void show();
    }

    // Уточнена абстракція 1: Трейлер фільму
    static class TrailerAd extends Advertisement {
        private final String movieTitle;
        private final String releaseDate;

        public TrailerAd(DisplayMedium medium, String movieTitle, String releaseDate) {
            super(medium);
            this.movieTitle = movieTitle;
            this.releaseDate = releaseDate;
        }

        @Override
        public void show() {
            String content = "Трейлер фільму «" + movieTitle + "» (у кіно з " + releaseDate + ")";
            medium.renderAd("ТРЕЙЛЕР", content, 120);
        }
    }

    // Уточнена абстракція 2: Реклама партнерів
    static class PartnerAd extends Advertisement {
        private final String partnerName;
        private final String offer;

        public PartnerAd(DisplayMedium medium, String partnerName, String offer) {
            super(medium);
            this.partnerName = partnerName;
            this.offer = offer;
        }

        @Override
        public void show() {
            String content = partnerName + " — " + offer;
            medium.renderAd("РЕКЛАМА ПАРТНЕРА", content, 30);
        }
    }

    // Уточнена абстракція 3: Соціальна реклама
    static class SocialAd extends Advertisement {
        private final String campaignTopic;

        public SocialAd(DisplayMedium medium, String campaignTopic) {
            super(medium);
            this.campaignTopic = campaignTopic;
        }

        @Override
        public void show() {
            medium.renderAd("СОЦІАЛЬНА РЕКЛАМА", campaignTopic, 45);
        }
    }

    // =========================================================
    // 3. ДЕМОНСТРАЦІЙНИЙ СЦЕНАРІЙ
    // =========================================================

    public static void main(String[] args) {
        // Створюємо доступні носії
        DisplayMedium hallScreen = new HallScreen();
        DisplayMedium lobbyScreens = new LobbyScreens();
        DisplayMedium mobileApp = new MobileApp();

        // 1. Показати трейлер на екрані зали і на екранах у фоє
        System.out.println("--- Крок 1: Трейлер на різних носіях ---");
        Advertisement duneTrailer = new TrailerAd(hallScreen, "Дюна: Месія", "18 грудня");
        duneTrailer.show(); // показ на екрані зали

        // Перемикаємо носій у runtime для того самого об'єкта трейлера
        duneTrailer.setMedium(lobbyScreens);
        duneTrailer.show(); // показ на екранах у фоє

        // 2. Показати соціальну рекламу в мобільному застосунку
        System.out.println("\n--- Крок 2: Соціальна реклама в застосунку ---");
        Advertisement socialCampaign = new SocialAd(
                mobileApp,
                "Підтримай притулок для тварин — скануй QR-код у застосунку"
        );
        socialCampaign.show();

        // 3. Додати носій «світлодіодний фасад» одним класом і показати на ньому рекламу партнера
        System.out.println("\n--- Крок 3: Новий носій (світлодіодний фасад) + реклама партнера ---");
        DisplayMedium ledFacade = new LedFacade();
        Advertisement partnerPromo = new PartnerAd(
                ledFacade,
                "Кав'ярня «Aroma»",
                "Знижка 20% на каву за квитком у кіно!"
        );
        partnerPromo.show();
    }
}