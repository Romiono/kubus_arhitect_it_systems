package ru.yourorg.cources;

import ru.yourorg.cources.model.Teacher;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Teacher t1 = Teacher.of(
                "T001",
                "Иванов",
                "Алексей",
                "Петрович",
                "+79160000001",
                "a.ivanov@example.com",
                LocalDate.of(2010, 9, 1),
                15,
                "Кандидат наук, доцент",
                "Специалист по Java"
        );

        Teacher t2 = Teacher.of(
                "T002",
                "Смирнова",
                "Елена",
                "Игоревна",
                "+79160000002",
                "e.smirnova@example.com",
                LocalDate.of(2015, 3, 15),
                10,
                "Старший преподаватель",
                "Практик по менеджменту"
        );

        String csv = "T003,Петров,Дмитрий,Александрович,+79160000003,d.petrov@example.com,2005-01-10,20,Профессор,Автор курсов";
        Teacher t3 = Teacher.fromCsv(csv);

        String json = "{\"staffNumber\":\"T004\",\"lastName\":\"Кузнецова\",\"firstName\":\"Мария\",\"patronymic\":\"Владимировна\",\"phone\":\"+79160000004\",\"email\":\"m.kuznetsova@example.com\",\"employmentStart\":\"2018-08-20\",\"experienceYears\":7,\"qualification\":\"Преподаватель\",\"notes\":\"Спец по коммуникациям\"}";
        Teacher t4 = Teacher.fromJson(json);

        System.out.println("=== Полные данные ===");
        System.out.println(t1.toStringFull());
        System.out.println(t2.toStringFull());
        System.out.println(t3.toStringFull());
        System.out.println(t4.toStringFull());

        System.out.println("\n=== Краткие данные ===");
        System.out.println(t1.toStringShort());
        System.out.println(t2.toStringShort());
        System.out.println(t3.toStringShort());
        System.out.println(t4.toStringShort());

        Teacher t1Copy = Teacher.of(
                "T001",
                "Иванов",
                "Алексей",
                "Петрович",
                "+79160000001",
                "a.ivanov@example.com",
                LocalDate.of(2010, 9, 1),
                15,
                "Кандидат наук, доцент",
                "Специалист по Java"
        );

        System.out.println("\nСравнение t1 и t1Copy: " + t1.equals(t1Copy));
        System.out.println("Сравнение t1 и t2: " + t1.equals(t2));
    }
}
