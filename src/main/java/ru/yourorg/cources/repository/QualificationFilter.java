package ru.yourorg.cources.repository;

import java.util.List;

public class QualificationFilter implements Filter {

  private final String qualification;

  public QualificationFilter(String qualification) {
    this.qualification = qualification;
  }

  @Override
  public String toSQLWhereClause() {
    return "qualification = ?";
  }

  @Override
  public List<Object> getParameters() {
    return List.of(qualification);
  }
}
