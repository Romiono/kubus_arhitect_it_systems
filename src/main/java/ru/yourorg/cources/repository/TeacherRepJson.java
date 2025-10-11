package ru.yourorg.cources.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TeacherRepJson extends TeacherRepository {

  private final File file;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private List<Teacher> teachers = new ArrayList<>();

  public TeacherRepJson(String filePath) {
    this.file = new File(filePath);
    readAll(); // загрузка данных при создании
  }

  // a. Чтение всех значений из файла
  public void readAll() {
    try {
      if (file.exists()) {
        teachers = objectMapper.readValue(file, new TypeReference<>() {});
      } else {
        teachers = new ArrayList<>();
      }
    } catch (IOException e) {
      throw new RuntimeException("Ошибка при чтении JSON-файла: " + e.getMessage(), e);
    }
  }

  // b. Запись всех значений в файл
  public void writeAll() {
    try {
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, teachers);
    } catch (IOException e) {
      throw new RuntimeException("Ошибка при записи JSON-файла: " + e.getMessage(), e);
    }
  }

  // c. Получить объект по ID
  public Teacher getById(String id) {
    return teachers.stream()
      .filter(t -> t.getStaffNumber().equals(id))
      .findFirst()
      .orElse(null);
  }

  @Override
  public List<TeacherSummary> get_k_n_short_list(Filter filter, SortOrder sortOrder, int k, int n) {
    return List.of();
  }

  // d. Получить список k по счету n объектов краткой версии
  public List<TeacherSummary> get_k_n_short_list(int k, int n) {
    return teachers.stream()
      .skip((long) (n - 1) * k)
      .limit(k)
      .map(TeacherSummary::fromTeacher)
      .collect(Collectors.toList());
  }

  // e. Сортировка по полю стажа
  public void sortByExperienceYears() {
    teachers.sort(Comparator.comparingInt(Teacher::getExperienceYears));
  }

  // f. Добавить объект в список (генерация нового ID)
  public void add(Teacher teacher) {
    String newId = generateNewId();
    Teacher newTeacher = Teacher.of(
      newId,
      teacher.getLastName(),
      teacher.getFirstName(),
      teacher.getPatronymic(),
      teacher.getPhone(),
      teacher.getEmail(),
      teacher.getEmploymentStart(),
      teacher.getExperienceYears(),
      teacher.getQualification(),
      teacher.getNotes()
    );
    teachers.add(newTeacher);
    writeAll();
  }

  private String generateNewId() {
    int maxId = teachers.stream()
      .map(Teacher::getStaffNumber)
      .filter(id -> id.matches("T\\d+"))
      .mapToInt(id -> Integer.parseInt(id.substring(1)))
      .max()
      .orElse(0);
    return "T" + String.format("%03d", maxId + 1);
  }

  // g. Заменить элемент по ID
  public boolean updateById(String id, Teacher updated) {
    for (int i = 0; i < teachers.size(); i++) {
      if (teachers.get(i).getStaffNumber().equals(id)) {
        Teacher newTeacher = Teacher.of(
          id,
          updated.getLastName(),
          updated.getFirstName(),
          updated.getPatronymic(),
          updated.getPhone(),
          updated.getEmail(),
          updated.getEmploymentStart(),
          updated.getExperienceYears(),
          updated.getQualification(),
          updated.getNotes()
        );
        teachers.set(i, newTeacher);
        writeAll();
        return true;
      }
    }
    return false;
  }

  // h. Удалить элемент по ID
  public boolean deleteById(String id) {
    boolean removed = teachers.removeIf(t -> t.getStaffNumber().equals(id));
    if (removed) writeAll();
    return removed;
  }

  @Override
  public int getCount(Filter filter) {
    return 0;
  }

  // i. Получить количество элементов
  public int getCount() {
    return teachers.size();
  }

  @Override
  public List<Teacher> getAllTeachers() {
    return new ArrayList<>(teachers); // можно вернуть ImmutableList если хочешь защиту от изменений
  }

}
