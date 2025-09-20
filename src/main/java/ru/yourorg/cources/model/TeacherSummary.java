package ru.yourorg.cources.model;

public class TeacherSummary {

    private final String shortName; // Фамилия + Инициалы
    private final String phone;
    private final String email;

    public TeacherSummary(String shortName, String phone, String email) {
        this.shortName = shortName;
        this.phone = phone;
        this.email = email;
    }

    // Фабричный метод из Teacher
    public static TeacherSummary fromTeacher(Teacher teacher) {
        return new TeacherSummary(
                teacher.getLastName() + " " + teacher.getFirstName().charAt(0) + ".",
                teacher.getPhone(),
                teacher.getEmail()
        );
    }

    // Геттеры
    public String getShortName() {
        return shortName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    // Краткий вывод на экран
    @Override
    public String toString() {
        return "TeacherSummary{" +
                "shortName='" + shortName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
