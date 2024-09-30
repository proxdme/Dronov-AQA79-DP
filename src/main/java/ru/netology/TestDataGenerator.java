package ru.netology;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestDataGenerator {

    // Метод для генерации актуального месяца
    public static String getValidMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    // Объединённый метод для генерации года
    public static String getYearWithOffset(int offset) {
        return LocalDate.now().plusYears(offset).format(DateTimeFormatter.ofPattern("yy"));
    }

    // Метод для получения номера одобренной карты
    public static String getApprovedCardNumber() {
        return "1111 2222 3333 4444";
    }

    // Метод для получения номера отклонённой карты
    public static String getDeclinedCardNumber() {
        return "5555 6666 7777 8888";
    }
}

