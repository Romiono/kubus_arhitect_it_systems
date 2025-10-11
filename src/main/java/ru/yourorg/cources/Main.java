package ru.yourorg.cources;

import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;
import ru.yourorg.cources.repository.*;
import ru.yourorg.cources.repository.decorators.TeacherFilter;
import ru.yourorg.cources.repository.decorators.TeacherRepJsonFilteredSortedDecorator;
import ru.yourorg.cources.repository.decorators.TeacherSorter;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

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
        System.out.println(t1.getShortName());
        System.out.println(t2.getShortName());
        System.out.println(t3.getShortName());
        System.out.println(t4.getShortName());

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

        System.out.println("\nСравнение t1 и t1Copy: " + t1.equals(t1Copy)); // true
        System.out.println("Сравнение t1 и t2: " + t1.equals(t2)); // false

        System.out.println("\nСокращенный вывод");
        TeacherSummary summary1 = TeacherSummary.fromTeacher(t1);
        System.out.println(summary1);

        TeacherSummary summaryCsv = TeacherSummary.fromTeacher(t3);
        System.out.println(summaryCsv);

      TeacherRepository baseRepo = new TeacherRepDB();
      TeacherRepository repo = new TeacherRepositoryDecorator(baseRepo);

      Filter filter = new QualificationFilter("PhD");
      SortOrder sortOrder = SortOrder.BY_EXPERIENCE_DESC;

      List<TeacherSummary> list = repo.get_k_n_short_list(filter, sortOrder, 10, 1);
      int count = repo.getCount(filter);

      TeacherRepository jsonRepo = new TeacherRepJson("teachers.json");

      TeacherFilter teacherFilter = teacher -> teacher.getExperienceYears() >= 5;
      TeacherSorter sorter = () -> Comparator.comparing(Teacher::getLastName);

      TeacherRepository filteredSortedRepo = new TeacherRepJsonFilteredSortedDecorator(jsonRepo, teacherFilter, sorter);

      List<TeacherSummary> page = filteredSortedRepo.get_k_n_short_list(10, 1);

      // int count = filteredSortedRepo.getCount();

    }
}
