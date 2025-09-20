package ru.yourorg.cources.model;

import ru.yourorg.cources.util.Validators;

import java.time.LocalDate;

public class Teacher {

    private final String staffNumber;
    private final String lastName;
    private final String firstName;
    private final String patronymic;
    private final String phone;
    private final String email;
    private final LocalDate employmentStart;
    private final int experienceYears;
    private final String qualification;
    private final String notes;

    // Приватный конструктор — объекты создаются только через фабрику
    private Teacher(String staffNumber, String lastName, String firstName, String patronymic,
                    String phone, String email, LocalDate employmentStart, int experienceYears,
                    String qualification, String notes) {
        this.staffNumber = staffNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.phone = phone;
        this.email = email;
        this.employmentStart = employmentStart;
        this.experienceYears = experienceYears;
        this.qualification = qualification;
        this.notes = notes;
    }

    // Фабричный метод с валидацией через Validators
    public static Teacher of(String staffNumber, String lastName, String firstName, String patronymic,
                             String phone, String email, LocalDate employmentStart, int experienceYears,
                             String qualification, String notes) {
        return new Teacher(
                Validators.requireNonEmpty(staffNumber, "Табельный номер"),
                Validators.requireNonEmpty(lastName, "Фамилия"),
                Validators.requireNonEmpty(firstName, "Имя"),
                patronymic != null ? patronymic.trim() : null,
                Validators.requireValidPhone(phone),
                Validators.requireValidEmail(email),
                Validators.requireNotFuture(employmentStart, "Дата начала работы"),
                Validators.requireNonNegative(experienceYears, "Стаж работы"),
                qualification != null ? qualification.trim() : null,
                notes != null ? notes.trim() : null
        );
    }

    // Геттеры
    public String getStaffNumber() {
        return staffNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getEmploymentStart() {
        return employmentStart;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public String getQualification() {
        return qualification;
    }

    public String getNotes() {
        return notes;
    }
}