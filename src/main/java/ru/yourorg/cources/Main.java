package ru.yourorg.cources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;
import ru.yourorg.cources.repository.Filter;
import ru.yourorg.cources.repository.SortOrder;
import ru.yourorg.cources.repository.TeacherRepository;
import ru.yourorg.cources.repository.TeacherRepDB;
import ru.yourorg.cources.repository.TeacherRepositoryDecorator;
import ru.yourorg.cources.repository.QualificationFilter;
import ru.yourorg.cources.repository.TeacherRepJson;
import ru.yourorg.cources.repository.decorators.TeacherFilter;
import ru.yourorg.cources.repository.decorators.TeacherRepJsonFilteredSortedDecorator;
import ru.yourorg.cources.repository.decorators.TeacherSorter;
import ru.yourorg.cources.util.JacksonObjectMapperProvider;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class Main {
  public static void main(String[] args) throws JsonProcessingException {
    System.out.println("==== Демонстрация системы учета преподавателей ====");

    // 1. Работа с моделью Teacher
    Teacher teacher1 = Teacher.of(
      "T001", "Иванов", "Алексей", "Петрович",
      "+79160000001", "ivanov@example.com",
      LocalDate.of(2010, 9, 1),
      14, "Профессор", "Эксперт по Java"
    );

    String csv = "T002,Сидорова,Марина,Владимировна,+79160000002,msidorova@example.com,2015-01-01,9,Доцент,Специалист по UX";
    Teacher teacher2 = Teacher.fromCsv(csv);

    String json = "{\"staffNumber\":\"T003\",\"lastName\":\"Кузнецов\",\"firstName\":\"Дмитрий\",\"patronymic\":\"Игоревич\",\"phone\":\"+79160000003\",\"email\":\"d.kuznecov@example.com\",\"employmentStart\":\"2018-02-14\",\"experienceYears\":6,\"qualification\":\"Преподаватель\",\"notes\":\"Фронтенд\"}";
    ObjectMapper mapper = JacksonObjectMapperProvider.getMapper();
    Teacher teacher3 = mapper.readValue(json, Teacher.class);

    System.out.println("\n-- Полные данные преподавателей --");
    System.out.println(teacher1.toStringFull());
    System.out.println(teacher2.toStringFull());
    System.out.println(teacher3.toStringFull());

    System.out.println("\n-- Сравнение объектов --");
    Teacher teacher1Copy = Teacher.of(
      "T001", "Иванов", "Алексей", "Петрович",
      "+79160000001", "ivanov@example.com",
      LocalDate.of(2010, 9, 1),
      14, "Профессор", "Эксперт по Java"
    );
    System.out.println("teacher1.equals(teacher1Copy): " + teacher1.equals(teacher1Copy)); // true

    // 2. Работа с базой данных через TeacherRepDB + декоратор
    TeacherRepository dbBaseRepo = new TeacherRepDB();
    TeacherRepository dbRepo = new TeacherRepositoryDecorator(dbBaseRepo);

    // 3. Добавление преподавателя в базу
    dbRepo.add(teacher1);

    // 4. Получение преподавателя по ID
    System.out.println("\n-- Преподаватель с ID T001 --");
    Teacher found = dbRepo.getById("T001");
    System.out.println(found);

    // 5. Использование фильтра и сортировки с помощью декоратора
    Filter filter = new QualificationFilter("Профессор");
    SortOrder sortOrder = SortOrder.BY_EXPERIENCE_DESC;

    List<TeacherSummary> filteredList = dbRepo.get_k_n_short_list(filter, sortOrder, 10, 1);
    int filteredCount = dbRepo.getCount(filter);

    System.out.println("\n-- Фильтрованные и отсортированные преподаватели из БД --");
    filteredList.forEach(System.out::println);
    System.out.println("Всего найдено: " + filteredCount);

    // 6. Работа с JSON файлом + декоратор
    TeacherRepository jsonRepo = new TeacherRepJson("teachers.json");
    jsonRepo.add(teacher2);
    jsonRepo.add(teacher3);

    TeacherFilter fileFilter = teacher -> teacher.getExperienceYears() >= 5;
    TeacherSorter fileSorter = () -> Comparator.comparing(Teacher::getLastName);

    TeacherRepository jsonDecorated = new TeacherRepJsonFilteredSortedDecorator(jsonRepo, fileFilter, fileSorter);

    List<TeacherSummary> pageFromFile = jsonDecorated.get_k_n_short_list(10, 1);
    int countFromFile = jsonDecorated.getCount();

    System.out.println("\n-- Преподаватели из JSON-файла (отфильтровано и отсортировано) --");
    pageFromFile.forEach(System.out::println);
    System.out.println("Всего преподавателей в файле (после фильтра): " + countFromFile);
  }
}
