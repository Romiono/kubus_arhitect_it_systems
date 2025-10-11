package ru.yourorg.cources.repository.decorators;

import ru.yourorg.cources.model.Teacher;

import java.util.Comparator;

@FunctionalInterface
public interface TeacherSorter {
  Comparator<Teacher> getComparator();
}
