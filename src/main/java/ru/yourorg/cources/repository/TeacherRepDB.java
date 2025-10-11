package ru.yourorg.cources.repository;

import ru.yourorg.cources.db.DBManager;
import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepDB extends TeacherRepository {

  private final Connection connection;

  public TeacherRepDB() {
    this.connection = DBManager.getInstance().getConnection();
  }

  @Override
  public void readAll() {
    throw new UnsupportedOperationException("Метод не реализован");
  }

  @Override
  public void writeAll() {
    throw new UnsupportedOperationException("Метод не реализован");
  }

  @Override
  public Teacher getById(String id) {
    String query = "SELECT * FROM teachers WHERE staff_number = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return parseTeacher(rs);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при получении преподавателя по ID", e);
    }
    return null;
  }

  @Override
  public List<TeacherSummary> get_k_n_short_list(Filter filter, SortOrder sortOrder, int k, int n) {
    return getFilteredSortedList(filter, sortOrder, k, n);
  }

  @Override
  public List<TeacherSummary> get_k_n_short_list(int k, int n) {
    return List.of();
  }

  @Override
  public void sortByExperienceYears() {
    // Не нужно — сортировка задается в запросах
  }

  @Override
  public void add(Teacher teacher) {
    String newId = generateNewId();
    // CHECKSTYLE:OFF
    String query = """
            INSERT INTO teachers (
                staff_number, last_name, first_name, patronymic, phone,
                email, employment_start, experience_years, qualification, notes
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    // CHECKSTYLE:ON

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, newId);
      stmt.setString(2, teacher.getLastName());
      stmt.setString(3, teacher.getFirstName());
      stmt.setString(4, teacher.getPatronymic());
      stmt.setString(5, teacher.getPhone());
      stmt.setString(6, teacher.getEmail());
      stmt.setDate(7, Date.valueOf(teacher.getEmploymentStart()));
      stmt.setInt(8, teacher.getExperienceYears());
      stmt.setString(9, teacher.getQualification());
      stmt.setString(10, teacher.getNotes());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при добавлении преподавателя", e);
    }
  }

  @Override
  public boolean updateById(String id, Teacher updated) {
    String query = """
            UPDATE teachers SET
                last_name = ?, first_name = ?, patronymic = ?, phone = ?,
                email = ?, employment_start = ?, experience_years = ?,
                qualification = ?, notes = ?
            WHERE staff_number = ?
            """;

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, updated.getLastName());
      stmt.setString(2, updated.getFirstName());
      stmt.setString(3, updated.getPatronymic());
      stmt.setString(4, updated.getPhone());
      stmt.setString(5, updated.getEmail());
      stmt.setDate(6, Date.valueOf(updated.getEmploymentStart()));
      stmt.setInt(7, updated.getExperienceYears());
      stmt.setString(8, updated.getQualification());
      stmt.setString(9, updated.getNotes());
      stmt.setString(10, id);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при обновлении преподавателя", e);
    }
  }

  @Override
  public boolean deleteById(String id) {
    String query = "DELETE FROM teachers WHERE staff_number = ?";
    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, id);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при удалении преподавателя", e);
    }
  }

  @Override
  public int getCount(Filter filter) {
    return getFilteredCount(filter);
  }

  @Override
  public int getCount() {
    return 0;
  }

  @Override
  public List<Teacher> getAllTeachers() {
    return List.of();
  }

  // ================= Вспомогательные методы ===================

  private Teacher parseTeacher(ResultSet rs) throws SQLException {
    return Teacher.of(
      rs.getString("staff_number"),
      rs.getString("last_name"),
      rs.getString("first_name"),
      rs.getString("patronymic"),
      rs.getString("phone"),
      rs.getString("email"),
      rs.getDate("employment_start").toLocalDate(),
      rs.getInt("experience_years"),
      rs.getString("qualification"),
      rs.getString("notes")
    );
  }

  private String generateNewId() {
    String query = "SELECT staff_number FROM teachers WHERE staff_number ~ '^T\\d+$'";
    try (Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

      int max = 0;
      while (rs.next()) {
        String id = rs.getString(1);
        int num = Integer.parseInt(id.substring(1));
        if (num > max) max = num;
      }
      return "T" + String.format("%03d", max + 1);

    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при генерации нового ID", e);
    }
  }

  public List<TeacherSummary> getFilteredSortedList(Filter filter, SortOrder sortOrder, int k, int n) {
    List<TeacherSummary> summaries = new ArrayList<>();
    StringBuilder query = new StringBuilder("SELECT * FROM teachers");

    if (filter != null) {
      query.append(" WHERE ").append(filter.toSQLWhereClause());
    }

    if (sortOrder != null) {
      query.append(" ORDER BY ").append(sortOrder.getSqlOrder());
    } else {
      query.append(" ORDER BY staff_number");
    }

    query.append(" LIMIT ? OFFSET ?");

    try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
      int idx = 1;
      if (filter != null) {
        for (Object param : filter.getParameters()) {
          stmt.setObject(idx++, param);
        }
      }
      stmt.setInt(idx++, k);
      stmt.setInt(idx, (n - 1) * k);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        summaries.add(TeacherSummary.fromTeacher(parseTeacher(rs)));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при получении списка с фильтром и сортировкой", e);
    }

    return summaries;
  }

  public int getFilteredCount(Filter filter) {
    StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM teachers");

    if (filter != null) {
      query.append(" WHERE ").append(filter.toSQLWhereClause());
    }

    try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
      if (filter != null) {
        int idx = 1;
        for (Object param : filter.getParameters()) {
          stmt.setObject(idx++, param);
        }
      }
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при подсчете количества с фильтром", e);
    }

    return 0;
  }
}
