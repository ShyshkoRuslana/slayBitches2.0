package laba12.task1;

public class main {

    private static void printNodeSummary(FestivalComponent node) {
        int duration = node.getDuration();
        int movies = node.getMovieCount();
        EventLogger.log("Scheduler", String.format(
                "Розрахунок для \"%s\": тривалість = %d хв, кількість фільмів = %d",
                node.getName(), duration, movies));
    }

    public static void main(String[] args) {
        EventLogger.log("Main", "=== Старт формування програми кінофестивалю «Кадр» ===");

        // Окремі фільми (Leaf)
        Movie mainOpening = new Movie("Тіні забутих предків", 97);
        Movie dramaEvening = new Movie("Додому", 96);

        Movie docFilm = new Movie("20 днів у Маріуполі", 95);
        Movie short1 = new Movie("Вхід через балкон", 16);
        Movie short2 = new Movie("Паперовий літак", 12);
        Movie short3 = new Movie("Кінотеатр на околиці", 18);

        // Збірка короткометражок (Composite рівня 3)
        ProgramGroup shortsCollection = new ProgramGroup("Збірка короткометражок", "Нова українська хвиля");
        shortsCollection.add(short1);
        shortsCollection.add(short2);
        shortsCollection.add(short3);

        // 2 тематичні блоки (Composite рівня 2)
        ProgramGroup block1 = new ProgramGroup("Тематичний блок", "Класика та драма");
        block1.add(mainOpening);
        block1.add(dramaEvening);

        ProgramGroup block2 = new ProgramGroup("Тематичний блок", "Документалістика та короткий метр");
        block2.add(docFilm);
        block2.add(shortsCollection);

        // Фестиваль (Composite рівня 1)
        ProgramGroup festival = new ProgramGroup("Фестиваль", "Кінофестиваль «Кадр 2026»");
        festival.add(block1);
        festival.add(block2);

        System.out.println("\n--- СТРУКТУРА ПРОГРАМИ ФЕСТИВАЛЮ (ДЕРЕВО) ---");
        festival.printTree("");

        System.out.println("\n--- ПЕРЕВІРКА ОДНАКОВОГО ВИКЛИКУ ДЛЯ РІЗНИХ РІВНІВ ---");
        printNodeSummary(festival);
        printNodeSummary(block2);
    }
}