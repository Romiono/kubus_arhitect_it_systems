package ru.yourorg.cources.repository.decorators;

import ru.yourorg.cources.model.Teacher;

@FunctionalInterface
public interface TeacherFilter {
  boolean test(Teacher teacher);
}
