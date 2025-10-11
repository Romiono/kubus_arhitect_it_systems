package ru.yourorg.cources.repository;

import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;

import java.util.List;

public abstract class TeacherRepository {

  public void readAll() {
    // по умолчанию ничего не делает (для БД)
  }

  public void writeAll() {
    // по умолчанию ничего не делает (для БД)
  }

  public abstract Teacher getById(String id);
  public abstract List<TeacherSummary> get_k_n_short_list(int k, int n);
  public abstract void sortByExperienceYears();
  public abstract void add(Teacher teacher);
  public abstract boolean updateById(String id, Teacher updated);
  public abstract boolean deleteById(String id);
  public abstract int getCount();
}
