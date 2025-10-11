package ru.yourorg.cources.repository;

import ru.yourorg.cources.model.Teacher;

import java.util.Comparator;

public enum SortOrder {
  BY_LASTNAME_ASC {
    @Override
    public String getSqlOrder() {
      return "last_name ASC";
    }

    @Override
    public Comparator<Teacher> getComparator() {
      return Comparator.comparing(Teacher::getLastName);
    }
  },

  BY_EXPERIENCE_DESC {
    @Override
    public String getSqlOrder() {
      return "experience_years DESC";
    }

    @Override
    public Comparator<Teacher> getComparator() {
      return Comparator.comparingInt(Teacher::getExperienceYears).reversed();
    }
  };

  public abstract String getSqlOrder();

  public abstract Comparator<Teacher> getComparator(); // 🔧 Добавь это
}
