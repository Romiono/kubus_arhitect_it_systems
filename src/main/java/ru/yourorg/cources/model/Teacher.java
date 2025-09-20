package ru.yourorg.cources.model;

import java.time.LocalDate;

public final class Teacher {
    private final int id;               // 0 — если ещё не сохранён в БД
    private final String lastName;
    private final String firstName;
    private final String patronymic;
    private final String phone;
    private final String email;
    private final LocalDate employmentStart;
    private final int experienceYears;
    private final String qualification;
    private final String notes;

    // Конструктор package-private — публичные фабрики/конструкторы в другом шаге
    Teacher(int id,
            String lastName,
            String firstName,
            String patronymic,
            String phone,
            String email,
            LocalDate employmentStart,
            int experienceYears,
            String qualification,
            String notes) {
        this.id = id;
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

    // геттеры
    public int getId(){ return id; }
    public String getLastName(){ return lastName; }
    public String getFirstName(){ return firstName; }
    public String getPatronymic(){ return patronymic; }
    public String getPhone(){ return phone; }
    public String getEmail(){ return email; }
    public LocalDate getEmploymentStart(){ return employmentStart; }
    public int getExperienceYears(){ return experienceYears; }
    public String getQualification(){ return qualification; }
    public String getNotes(){ return notes; }
}
