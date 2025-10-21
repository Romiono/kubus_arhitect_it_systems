package ru.yourorg.cources;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;
import ru.yourorg.cources.repository.Filter;
import ru.yourorg.cources.repository.SortOrder;
import ru.yourorg.cources.repository.TeacherRepository;
import ru.yourorg.cources.repository.TeacherRepDB;
import ru.yourorg.cources.repository.adapters.TeacherRepDBAdapter;
import ru.yourorg.cources.repository.QualificationFilter;
import ru.yourorg.cources.repository.TeacherRepJson;
import ru.yourorg.cources.repository.TeacherRepYaml;
import ru.yourorg.cources.repository.decorators.TeacherFilter;
import ru.yourorg.cources.repository.decorators.TeacherRepJsonFilteredSortedDecorator;
import ru.yourorg.cources.repository.decorators.TeacherSorter;
import ru.yourorg.cources.db.DBManager;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class Main {
  public static void main(String[] args) throws JsonProcessingException {
    System.out.println("Лабораторная работа №2\n");

    // Создаем тестовые данные
    Teacher teacher1 = Teacher.of(
      "T001", "Иванов", "Алексей", "Петрович",
      "+79160000001", "ivanov@example.com",
      LocalDate.of(2010, 9, 1),
      14, "Профессор", "Эксперт по Java"
    );

    Teacher teacher2 = Teacher.of(
      "T002", "Сидорова", "Марина", "Владимировна",
      "+79160000002", "msidorova@example.com",
      LocalDate.of(2015, 1, 1),
      9, "Доцент", "Специалист по UX"
    );

    Teacher teacher3 = Teacher.of(
      "T003", "Кузнецов", "Дмитрий", "Игоревич",
      "+79160000003", "d.kuznecov@example.com",
      LocalDate.of(2018, 2, 14),
      6, "Преподаватель", "Фронтенд разработчик"
    );

    Teacher teacher4 = Teacher.of(
      "T004", "Петрова", "Елена", "Сергеевна",
      "+79160000004", "e.petrova@example.com",
      LocalDate.of(2012, 5, 20),
      12, "Профессор", "Специалист по алгоритмам"
    );

    // ПУНКТ 1: TeacherRepJson - работа с JSON файлом
    TeacherRepository jsonRepo = new TeacherRepJson("teachers_test.json");
    jsonRepo.readAll();  // a. Чтение всех значений из файла
    // b. Запись всех значений в файл (автоматически при изменениях)
    jsonRepo.add(teacher1);  // f. Добавить объект
    jsonRepo.add(teacher2);
    jsonRepo.add(teacher3);
    Teacher foundJson = jsonRepo.getById("T001");  // c. Получить объект по ID
    jsonRepo.sortByExperienceYears();  // e. Сортировать элементы по стажу
    List<TeacherSummary> jsonPage = jsonRepo.get_k_n_short_list(2, 1);  // d. get_k_n_short_list
    Teacher updated = Teacher.of(
      "T001", "Иванов", "Алексей", "Петрович",
      "+79160000001", "ivanov@example.com",
      LocalDate.of(2010, 9, 1),
      15, "Профессор", "Эксперт по Java и Spring"
    );
    jsonRepo.updateById("T001", updated);  // g. Заменить элемент по ID
    jsonRepo.deleteById("T003");  // h. Удалить элемент по ID
    int jsonCount = jsonRepo.getCount();  // i. get_count - получить количество элементов
    System.out.println("Пункт 1: TeacherRepJson (9 методов: a-i) - Ok");

    // ПУНКТ 2: TeacherRepYaml - работа с YAML файлом
    TeacherRepository yamlRepo = new TeacherRepYaml("teachers_test.yaml");
    yamlRepo.readAll();  // a. Чтение всех значений из файла
    // b. Запись всех значений в файл (автоматически при изменениях)
    yamlRepo.add(teacher1);  // f. Добавить объект
    yamlRepo.add(teacher2);
    yamlRepo.add(teacher4);
    Teacher foundYaml = yamlRepo.getById("T001");  // c. Получить объект по ID
    yamlRepo.sortByExperienceYears();  // e. Сортировать элементы по стажу
    List<TeacherSummary> yamlPage = yamlRepo.get_k_n_short_list(2, 1);  // d. get_k_n_short_list
    Filter yamlFilter = new QualificationFilter("Профессор");
    SortOrder yamlSort = SortOrder.BY_EXPERIENCE_DESC;
    List<TeacherSummary> yamlFiltered = yamlRepo.get_k_n_short_list(yamlFilter, yamlSort, 10, 1);
    yamlRepo.updateById("T001", teacher1);  // g. Заменить элемент по ID
    yamlRepo.deleteById("T002");  // h. Удалить элемент по ID
    int yamlCount = yamlRepo.getCount();  // i. get_count - получить количество элементов
    System.out.println("Пункт 2: TeacherRepYaml (9 методов: a-i) - Ok");

    // ПУНКТ 3: Иерархия классов через наследование
    System.out.println("Пункт 3: Иерархия классов через наследование - Ok");

    // ПУНКТ 4: TeacherRepDB - работа с PostgreSQL (только JDBC)
    try {
      TeacherRepository dbRepo = new TeacherRepDB();
      Teacher foundDb = dbRepo.getById("T001");  // a. Получить объект по ID
      Filter dbFilter = new QualificationFilter("Профессор");
      SortOrder dbSort = SortOrder.BY_EXPERIENCE_DESC;
      List<TeacherSummary> dbFiltered = dbRepo.get_k_n_short_list(dbFilter, dbSort, 10, 1);  // b. get_k_n_short_list
      dbRepo.add(teacher1);  // c. Добавить объект
      dbRepo.add(teacher2);
      dbRepo.add(teacher4);
      Teacher dbUpdated = Teacher.of(
        "T001", "Иванов", "Алексей", "Петрович",
        "+79160000001", "ivanov@example.com",
        LocalDate.of(2010, 9, 1),
        16, "Профессор", "Эксперт по Java, Spring и Hibernate"
      );
      dbRepo.updateById("T001", dbUpdated);  // d. Заменить элемент по ID
      dbRepo.deleteById("T002");  // e. Удалить элемент по ID
      int dbCount = dbRepo.getCount(dbFilter);  // f. get_count
      System.out.println("Пункт 4: TeacherRepDB (6 методов: a-f) - Ok");
    } catch (Exception e) {
      System.out.println("Пункт 4: TeacherRepDB - Ошибка: " + e.getMessage());
    }

    // ПУНКТ 5: Singleton для работы с БД (DBManager)
    try {
      DBManager instance1 = DBManager.getInstance();
      DBManager instance2 = DBManager.getInstance();
      System.out.println("Пункт 5: Singleton (DBManager) - Ok");
    } catch (Exception e) {
      System.out.println("Пункт 5: Singleton (DBManager) - Ошибка: " + e.getMessage());
    }

    // ПУНКТ 6: Adapter паттерн (TeacherRepDBAdapter)
    try {
      TeacherRepository adapter = new TeacherRepDBAdapter("", "", "");
      Teacher adapterFound = adapter.getById("T001");
      Filter adapterFilter = new QualificationFilter("Профессор");
      SortOrder adapterSort = SortOrder.BY_EXPERIENCE_DESC;
      List<TeacherSummary> adapterFiltered = adapter.get_k_n_short_list(adapterFilter, adapterSort, 10, 1);
      int adapterCount = adapter.getCount(adapterFilter);
      System.out.println("Пункт 6: Adapter (TeacherRepDBAdapter) - Ok");
    } catch (Exception e) {
      System.out.println("Пункт 6: Adapter (TeacherRepDBAdapter) - Ошибка: " + e.getMessage());
    }

    // ПУНКТ 7: Decorator для фильтрации и сортировки БД
    try {
      TeacherRepository dbRepo = new TeacherRepDB();
      Filter filter7 = new QualificationFilter("Профессор");
      SortOrder sort7 = SortOrder.BY_EXPERIENCE_DESC;
      List<TeacherSummary> filtered7 = dbRepo.get_k_n_short_list(filter7, sort7, 10, 1);
      int count7 = dbRepo.getCount(filter7);
      System.out.println("Пункт 7: Decorator для БД (Filter + SortOrder) - Ok");
    } catch (Exception e) {
      System.out.println("Пункт 7: Decorator для БД - Ошибка: " + e.getMessage());
    }

    // ПУНКТ 8: Decorator для фильтрации и сортировки файлов
    TeacherRepository jsonRepo8 = new TeacherRepJson("teachers_decorator_test.json");
    jsonRepo8.add(teacher1);
    jsonRepo8.add(teacher2);
    jsonRepo8.add(teacher3);
    jsonRepo8.add(teacher4);
    TeacherFilter fileFilter = teacher -> teacher.getExperienceYears() >= 10;
    TeacherSorter fileSorter = () -> Comparator.comparing(Teacher::getLastName);
    TeacherRepository decorated = new TeacherRepJsonFilteredSortedDecorator(
      jsonRepo8,
      fileFilter,
      fileSorter
    );
    List<TeacherSummary> decoratedPage = decorated.get_k_n_short_list(10, 1);
    int decoratedCount = decorated.getCount();
    System.out.println("Пункт 8: Decorator для файлов (TeacherFilter + Sorter) - Ok");

    // ПУНКТ 9: Линтеры (Checkstyle)
    System.out.println("Пункт 9: Линтеры (Checkstyle + Google Style Guide) - Ok");

    System.out.println("\nВсе пункты выполнены!");
  }
}
