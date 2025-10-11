package ru.yourorg.cources.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TeacherRepYaml extends TeacherRepository {

  private final File file;
  private final ObjectMapper yamlMapper;
  private List<Teacher> teachers = new ArrayList<>();

  public TeacherRepYaml(String filePath) {
    this.file = new File(filePath);
    this.yamlMapper = new ObjectMapper(new YAMLFactory());
    readAll();
  }

  // a. Чтение всех значений из файла
  public void readAll() {
    try {
      if (file.exists()) {
        teachers = yamlMapper.readValue(file, new TypeReference<>() {});
      } else {
        teachers = new ArrayList<>();
      }
    } catch (IOException e) {
      throw new RuntimeException("Ошибка при чтении YAML-файла: " + e.getMessage(), e);
    }
  }

  // b. Запись всех значений в файл
  public void writeAll() {
    try {
      yamlMapper.writerWithDefaultPrettyPrinter().writeValue(file, teachers);
    } catch (IOException e) {
      throw new RuntimeException("Ошибка при записи YAML-файла: " + e.getMessage(), e);
    }
  }

  // c. Получить объект по ID
  public Teacher getById(String id) {
    return teachers.stream()
      .filter(t -> t.getStaffNumber().equals(id))
      .findFirst()
      .orElse(null);
  }

  // d. Получить список k по счету n объектов краткой версии
  public List<TeacherSummary> get_k_n_short_list(int k, int n) {
    return teachers.stream()
      .skip((long) (n - 1) * k)
      .limit(k)
      .map(TeacherSummary::fromTeacher)
      .collect(Collectors.toList());
  }

  // e. Сортировка по стажу
  public void sortByExperienceYears() {
    teachers.sort(Comparator.comparingInt(Teacher::getExperienceYears));
  }

  // f. Добавить объект в список
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

  // g. Заменить по ID
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

  // h. Удаление по ID
  public boolean deleteById(String id) {
    boolean removed = teachers.removeIf(t -> t.getStaffNumber().equals(id));
    if (removed) writeAll();
    return removed;
  }

  // i. Количество элементов
  public int getCount() {
    return teachers.size();
  }
}
