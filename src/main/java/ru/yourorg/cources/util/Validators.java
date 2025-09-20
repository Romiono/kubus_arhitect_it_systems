package ru.yourorg.cources.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

public final class Validators {
    private Validators(){}

    private static final Pattern EMAIL = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE = Pattern.compile("^\\+?\\d[\\d\\-\\s()]{6,}$");

    public static String requireName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
        String trimmed = name.trim();
        if (trimmed.length() > 100) throw new IllegalArgumentException(fieldName + " слишком длинное");
        return trimmed;
    }

    public static String requirePhone(String phone) {
        if (phone == null) return null;
        String p = phone.trim();
        if (!PHONE.matcher(p).matches()) throw new IllegalArgumentException("Неправильный формат телефона");
        return p;
    }

    public static String requireEmail(String email) {
        if (email == null) return null;
        String e = email.trim();
        if (!EMAIL.matcher(e).matches()) throw new IllegalArgumentException("Неправильный формат email");
        return e;
    }

    public static int requireNonNegative(int value, String fieldName) {
        if (value < 0) throw new IllegalArgumentException(fieldName + " должно быть >= 0");
        return value;
    }

    public static LocalDate requireDateNotInFuture(LocalDate date, String fieldName) {
        if (date == null) return null;
        if (date.isAfter(LocalDate.now())) throw new IllegalArgumentException(fieldName + " не может быть в будущем");
        return date;
    }
}
