package laba16.task1;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task1Runner {

    public static void runTask1() {
        System.out.println("=== ЗАВДАННЯ 1: ОБХІД РОЗКЛАДУ СЕАНСІВ (ШАБЛОН ITERATOR) ===\n");

        CinemaSchedule schedule = new CinemaSchedule();
        LocalDate today = LocalDate.of(2026, 11, 20);
        LocalDate tomorrow = LocalDate.of(2026, 11, 21);

        EventLogger.log("Ініціалізація", "Стан розкладу ДО наповнення:");
        schedule.printInternalStructureSummary();

        // Наповнюємо розклад по різних залах (включно з новою Залою №5) та днях
        schedule.addSession(new Session("Дюна: Частина друга", "Зала №1 (IMAX)", today, LocalTime.of(18, 30), 15));
        schedule.addSession(new Session("Інтерстеллар", "Зала №2", today, LocalTime.of(14, 00), 0)); // Місць немає
        schedule.addSession(new Session("Дюна: Частина друга", "Зала №5 (VIP)", today, LocalTime.of(12, 15), 4));
        schedule.addSession(new Session("Оппенгеймер", "Зала №3", today, LocalTime.of(21, 00), 22));
        schedule.addSession(new Session("Дюна: Частина друга", "Зала №1 (IMAX)", tomorrow, LocalTime.of(19, 00), 40));

        EventLogger.log("Ініціалізація", "Стан розкладу ПІСЛЯ наповнення:");
        schedule.printInternalStructureSummary();
        System.out.println();

        // 1. Вивести всі сеанси на день у хронологічному порядку з усіх зал (для сайту)
        EventLogger.log("Модуль: Сайт", "Запит: усі сеанси на " + today + " у хронологічному порядку");
        SessionIterator chronoIterator = schedule.createChronologicalIterator(today);
        while (chronoIterator.hasNext()) {
            EventLogger.log("Сайт -> Сеанс", chronoIterator.next().toString());
        }
        System.out.println();

        // 2. Вивести сеанси одного фільму (для мобільного застосунку)
        EventLogger.log("Модуль: Застосунок", "Запит: усі сеанси фільму «Дюна: Частина друга»");
        SessionIterator movieIterator = schedule.createMovieIterator("Дюна: Частина друга");
        while (movieIterator.hasNext()) {
            EventLogger.log("Застосунок -> Сеанс", movieIterator.next().toString());
        }
        System.out.println();

        // 3. Вивести сеанси з вільними місцями і одночасно вести інший обхід
        EventLogger.log("Модуль: Каса + Сайт", "Демонстрація двох одночасних незалежних обходів:");
        SessionIterator cashierIterator = schedule.createAvailableSeatsIterator();
        SessionIterator parallelSiteIterator = schedule.createChronologicalIterator(today);

        int step = 1;
        while (cashierIterator.hasNext() || parallelSiteIterator.hasNext()) {
            EventLogger.log("Паралельний обхід", "--- Крок " + step + " ---");
            if (cashierIterator.hasNext()) {
                EventLogger.log("Касир (вільні місця)", cashierIterator.next().toString());
            }
            if (parallelSiteIterator.hasNext()) {
                EventLogger.log("Сайт (хронологія)", parallelSiteIterator.next().toString());
            }
            step++;
        }

        System.out.println();
        EventLogger.log("Завершення", "Стан розкладу ПІСЛЯ всіх обходів (колекція не змінилася):");
        schedule.printInternalStructureSummary();
    }

    public static void main(String[] args) {
        runTask1();
    }
}
