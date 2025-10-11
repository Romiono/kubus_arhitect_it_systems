package ru.yourorg.cources.repository;

public enum SortOrder {
  BY_EXPERIENCE_DESC("experience_years DESC"),
  BY_EXPERIENCE_ASC("experience_years ASC"),
  BY_LAST_NAME_ASC("last_name ASC"),
  BY_LAST_NAME_DESC("last_name DESC");

  private final String sqlOrder;

  SortOrder(String sqlOrder) {
    this.sqlOrder = sqlOrder;
  }

  public String getSqlOrder() {
    return sqlOrder;
  }
}
