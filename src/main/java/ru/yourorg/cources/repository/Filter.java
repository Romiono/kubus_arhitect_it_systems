package ru.yourorg.cources.repository;

import ru.yourorg.cources.model.Teacher;

import java.util.List;

public interface Filter {
  // Для SQL (уже есть)
  String toSQLWhereClause();

  List<Object> getParameters();

  // 🔧 Добавь это:
  boolean apply(Teacher teacher); // <-- нужен для JSON-фильтрации
}
