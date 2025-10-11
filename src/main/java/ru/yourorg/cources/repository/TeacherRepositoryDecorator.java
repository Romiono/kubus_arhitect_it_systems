package ru.yourorg.cources.repository;

import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;

import java.util.List;

public class TeacherRepositoryDecorator extends TeacherRepository {

  private final TeacherRepository baseRepository;

  public TeacherRepositoryDecorator(TeacherRepository baseRepository) {
    this.baseRepository = baseRepository;
  }

  @Override
  public void readAll() {
    baseRepository.readAll();
  }

  @Override
  public void writeAll() {
    baseRepository.writeAll();
  }

  @Override
  public Teacher getById(String id) {
    return baseRepository.getById(id);
  }

  @Override
  public List<TeacherSummary> get_k_n_short_list(Filter filter, SortOrder sortOrder, int k, int n) {
    if (!(baseRepository instanceof TeacherRepDB)) {
      throw new UnsupportedOperationException("Фильтрация и сортировка поддерживаются только для DB репозитория");
    }
    return baseRepository.get_k_n_short_list(filter, sortOrder, k, n);
  }

  @Override
  public List<TeacherSummary> get_k_n_short_list(int k, int n) {
    return List.of();
  }

  @Override
  public void sortByExperienceYears() {
    baseRepository.sortByExperienceYears();
  }

  @Override
  public void add(Teacher teacher) {
    baseRepository.add(teacher);
  }

  @Override
  public boolean updateById(String id, Teacher updated) {
    return baseRepository.updateById(id, updated);
  }

  @Override
  public boolean deleteById(String id) {
    return baseRepository.deleteById(id);
  }

  @Override
  public int getCount(Filter filter) {
    if (!(baseRepository instanceof TeacherRepDB)) {
      throw new UnsupportedOperationException("Фильтрация поддерживается только для DB репозитория");
    }
    return baseRepository.getCount(filter);
  }

  @Override
  public int getCount() {
    return 0;
  }

  @Override
  public List<Teacher> getAllTeachers() {
    return List.of();
  }
}
