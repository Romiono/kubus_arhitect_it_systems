package ru.yourorg.cources.repository.decorators;

import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;
import ru.yourorg.cources.repository.TeacherRepository;

import java.util.List;

public abstract class TeacherRepositoryDecorator extends TeacherRepository {

  protected final TeacherRepository baseRepo;

  public TeacherRepositoryDecorator(TeacherRepository baseRepo) {
    this.baseRepo = baseRepo;
  }

  @Override public void readAll() { baseRepo.readAll(); }
  @Override public void writeAll() { baseRepo.writeAll(); }
  @Override public Teacher getById(String id) { return baseRepo.getById(id); }
  @Override public void sortByExperienceYears() { baseRepo.sortByExperienceYears(); }
  @Override public void add(Teacher teacher) { baseRepo.add(teacher); }
  @Override public boolean updateById(String id, Teacher updated) { return baseRepo.updateById(id, updated); }
  @Override public boolean deleteById(String id) { return baseRepo.deleteById(id); }
  @Override public List<Teacher> getAllTeachers() { return baseRepo.getAllTeachers(); }
}
