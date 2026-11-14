package laba12.task1;

public interface FestivalComponent {
    String getName();
    int getDuration();      // Загальна тривалість у хвилинах
    int getMovieCount();    // Загальна кількість фільмів
    void printTree(String indent);
}
