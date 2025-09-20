package ru.yourorg.cources.model;

import ru.yourorg.cources.util.Validators;

public abstract class Person {

    private final String lastName;
    private final String firstName;
    private final String patronymic;
    private final String phone;
    private final String email;

    protected Person(String lastName, String firstName, String patronymic,
                     String phone, String email) {
        this.lastName = Validators.requireNonEmpty(lastName, "Фамилия");
        this.firstName = Validators.requireNonEmpty(firstName, "Имя");
        this.patronymic = patronymic != null ? patronymic.trim() : null;
        this.phone = Validators.requireValidPhone(phone);
        this.email = Validators.requireValidEmail(email);
    }

    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getPatronymic() { return patronymic; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    // Краткая версия имени
    public String getShortName() {
        return lastName + " " + firstName.charAt(0) + ".";
    }

    // Краткий вывод
    @Override
    public String toString() {
        return getShortName() + " | " + phone + " | " + email;
    }
}
