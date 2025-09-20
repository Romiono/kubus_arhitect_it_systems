package ru.yourorg.cources.model;

public class TeacherSummary extends Person {

    public TeacherSummary(String lastName, String firstName, String patronymic, String phone, String email) {
        super(lastName, firstName, patronymic, phone, email);
    }

    // Фабричный метод из Teacher
    public static TeacherSummary fromTeacher(Teacher teacher) {
        return new TeacherSummary(
                teacher.getLastName(),
                teacher.getFirstName(),
                teacher.getPatronymic(),
                teacher.getPhone(),
                teacher.getEmail()
        );
    }

    @Override
    public String toString() {
        return super.toString(); // краткая версия
    }
}
