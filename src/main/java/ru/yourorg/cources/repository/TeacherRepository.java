package ru.yourorg.cources.repository;

import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;

import java.util.List;

public abstract class TeacherRepository {

  public abstract void readAll(); // a
  public abstract void writeAll(); // b
  public abstract Teacher getById(String id); // c
  public abstract List<TeacherSummary> get_k_n_short_list(int k, int n); // d
  public abstract void sortByExperienceYears(); // e
  public abstract void add(Teacher teacher); // f
  public abstract boolean updateById(String id, Teacher updated); // g
  public abstract boolean deleteById(String id); // h
  public abstract int getCount(); // i
}
