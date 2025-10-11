package ru.yourorg.cources.repository;

import java.util.List;

public interface Filter {
  String toSQLWhereClause();
  List<Object> getParameters();
}
