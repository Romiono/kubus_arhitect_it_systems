package ru.yourorg.cources.util;

import java.time.LocalDate;

public class Validators {

    // Проверка, что строка не null и не пустая
    public static String requireNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
        return value.trim();
    }

    // Проверка на положительное число
    public static int requireNonNegative(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " не может быть отрицательным");
        }
        return value;
    }

    // Проверка даты — не в будущем
    public static LocalDate requireNotFuture(LocalDate date, String fieldName) {
        if (date != null && date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(fieldName + " не может быть в будущем");
        }
        return date;
    }

    // Проверка email (очень простая)
    public static String requireValidEmail(String email) {
        if (email != null && !email.contains("@")) {
            throw new IllegalArgumentException("Email некорректен");
        }
        return email;
    }

    // Проверка телефона (очень простая)
    public static String requireValidPhone(String phone) {
        if (phone != null && !phone.matches("\\+?\\d{7,15}")) {
            throw new IllegalArgumentException("Телефон некорректен");
        }
        return phone;
    }
}
