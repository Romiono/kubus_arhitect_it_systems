package ru.yourorg.cources.repository.decorators;

import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;
import ru.yourorg.cources.repository.Filter;
import ru.yourorg.cources.repository.SortOrder;
import ru.yourorg.cources.repository.TeacherRepository;

import java.util.List;
import java.util.stream.Collectors;

public class TeacherRepJsonFilteredSortedDecorator extends TeacherRepositoryDecorator {

  private final TeacherFilter filter;
  private final TeacherSorter sorter;

  public TeacherRepJsonFilteredSortedDecorator(
    TeacherRepository baseRepo,
    TeacherFilter filter,
    TeacherSorter sorter) {
    super(baseRepo);
    this.filter = filter;
    this.sorter = sorter;
  }

  @Override
  public List<TeacherSummary> get_k_n_short_list(Filter filter, SortOrder sortOrder, int k, int n) {
    return List.of();
  }

  @Override
  public List<TeacherSummary> get_k_n_short_list(int k, int n) {
    return baseRepo.getAllTeachers().stream()
      .filter(filter::test)
      .sorted(sorter.getComparator())
      .skip((long) (n - 1) * k)
      .limit(k)
      .map(TeacherSummary::fromTeacher)
      .collect(Collectors.toList());
  }

  @Override
  public int getCount(Filter filter) {
    return 0;
  }

  @Override
  public int getCount() {
    return (int) baseRepo.getAllTeachers().stream()
      .filter(filter::test)
      .count();
  }
}
