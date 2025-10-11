package ru.yourorg.cources.repository;

import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;

import java.util.List;

public class TeacherRepDBAdapter extends TeacherRepository {

  private final TeacherRepDB dbRepo;

  public TeacherRepDBAdapter(String url, String user, String password) {
    this.dbRepo = new TeacherRepDB();
  }

  @Override
  public void readAll() {
    throw new UnsupportedOperationException("readAll не поддерживается в DB-адаптере");
  }

  @Override
  public void writeAll() {
    // В БД это не используется
  }

  @Override
  public Teacher getById(String id) {
    return dbRepo.getById(id);
  }

  @Override
  public List<TeacherSummary> get_k_n_short_list(Filter filter, SortOrder sortOrder, int k, int n) {
    return List.of();
  }

  @Override
  public List<TeacherSummary> get_k_n_short_list(int k, int n) {
    return dbRepo.get_k_n_short_list(k, n);
  }

  @Override
  public void sortByExperienceYears() {
    // В БД это реализуется через SQL (ORDER BY), не нужно вручную
  }

  @Override
  public void add(Teacher teacher) {
    dbRepo.add(teacher);
  }

  @Override
  public boolean updateById(String id, Teacher updated) {
    return dbRepo.updateById(id, updated);
  }

  @Override
  public boolean deleteById(String id) {
    return dbRepo.deleteById(id);
  }

  @Override
  public int getCount(Filter filter) {
    return 0;
  }

  @Override
  public int getCount() {
    return dbRepo.getCount();
  }
}
