package ru.yourorg.cources.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;
import ru.yourorg.cources.util.JacksonObjectMapperProvider;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TeacherRepJson extends TeacherRepository {

  private final File file;
  private final ObjectMapper objectMapper;
  private List<Teacher> teachers = new ArrayList<>();

  public TeacherRepJson(String filePath) {
    this.file = new File(filePath);
    this.objectMapper = JacksonObjectMapperProvider.getMapper();;
    this.objectMapper.registerModule(new JavaTimeModule());
    this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    readAll(); // Загрузка при создании
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
  @Override
  public Teacher getById(String id) {
    return teachers.stream()
      .filter(t -> t.getStaffNumber().equals(id))
      .findFirst()
      .orElse(null);
  }

  // d. Получить краткий список с фильтром и сортировкой
  @Override
  public List<TeacherSummary> get_k_n_short_list(Filter filter, SortOrder sortOrder, int k, int n) {
    Stream<Teacher> stream = teachers.stream();

    if (filter != null) {
      stream = stream.filter(filter::apply);
    }

    if (sortOrder != null) {
      Comparator<Teacher> comparator = sortOrder.getComparator();
      if (comparator != null) {
        stream = stream.sorted(comparator);
      }
    }

    return stream
      .skip((long) (n - 1) * k)
      .limit(k)
      .map(TeacherSummary::fromTeacher)
      .collect(Collectors.toList());
  }

  // e. Получить краткий список без фильтра
  @Override
  public List<TeacherSummary> get_k_n_short_list(int k, int n) {
    return teachers.stream()
      .skip((long) (n - 1) * k)
      .limit(k)
      .map(TeacherSummary::fromTeacher)
      .collect(Collectors.toList());
  }

  // f. Сортировка по стажу (по возрастанию)
  @Override
  public void sortByExperienceYears() {
    teachers.sort(Comparator.comparingInt(Teacher::getExperienceYears));
  }

  // g. Добавить преподавателя (с генерацией нового ID)
  @Override
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

  // h. Обновить преподавателя по ID
  @Override
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

  // i. Удалить по ID
  @Override
  public boolean deleteById(String id) {
    boolean removed = teachers.removeIf(t -> t.getStaffNumber().equals(id));
    if (removed) {
      writeAll();
    }
    return removed;
  }

  // j. Кол-во по фильтру
  @Override
  public int getCount(Filter filter) {
    if (filter == null) return teachers.size();
    return (int) teachers.stream().filter(filter::apply).count();
  }

  // k. Общее кол-во
  @Override
  public int getCount() {
    return teachers.size();
  }

  // l. Получить всех преподавателей
  @Override
  public List<Teacher> getAllTeachers() {
    return new ArrayList<>(teachers);
  }
}
