package ru.yourorg.cources.repository;

import ru.yourorg.cources.model.Teacher;
import ru.yourorg.cources.model.TeacherSummary;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TeacherRepDB extends TeacherRepository {

  private final String url;
  private final String user;
  private final String password;

  public TeacherRepDB(String url, String user, String password) {
    this.url = url;
    this.user = user;
    this.password = password;
  }

  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, user, password);
  }

  // a. Получить объект по ID
  @Override
  public Teacher getById(String id) {
    String sql = "SELECT * FROM courses.teachers WHERE staff_number = ?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, id);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return extractTeacher(rs);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при получении преподавателя: " + e.getMessage(), e);
    }
    return null;
  }

  // b. Получить список k по счету n кратко
  @Override
  public List<TeacherSummary> get_k_n_short_list(int k, int n) {
    String sql = """
            SELECT * FROM courses.teachers
            ORDER BY experience_years DESC
            OFFSET ? LIMIT ?
        """;
    List<TeacherSummary> list = new ArrayList<>();
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setInt(1, (n - 1) * k);
      stmt.setInt(2, k);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        list.add(TeacherSummary.fromTeacher(extractTeacher(rs)));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при получении списка: " + e.getMessage(), e);
    }
    return list;
  }

  @Override
  public void sortByExperienceYears() {
    throw new UnsupportedOperationException("Сортировка выполняется в запросе к БД через get_k_n_short_list");
  }


  // c. Добавить объект в список (сформировать ID)
  @Override
  public void add(Teacher teacher) {
    String newId = generateNewId();
    String sql = """
            INSERT INTO courses.teachers (
                staff_number, last_name, first_name, patronymic, phone,
                email, employment_start, experience_years, qualification, notes
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
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
      throw new RuntimeException("Ошибка при добавлении преподавателя: " + e.getMessage(), e);
    }
  }

  private String generateNewId() {
    String sql = "SELECT MAX(staff_number) FROM courses.teachers WHERE staff_number ~ '^T\\\\d{3}$'";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
      if (rs.next()) {
        String maxId = rs.getString(1);
        int num = (maxId != null) ? Integer.parseInt(maxId.substring(1)) : 0;
        return "T" + String.format("%03d", num + 1);
      }
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при генерации ID: " + e.getMessage(), e);
    }
    return "T001";
  }

  // d. Заменить элемент по ID
  @Override
  public boolean updateById(String id, Teacher updated) {
    String sql = """
            UPDATE courses.teachers SET
                last_name=?, first_name=?, patronymic=?, phone=?, email=?,
                employment_start=?, experience_years=?, qualification=?, notes=?
            WHERE staff_number=?
        """;
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
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
      throw new RuntimeException("Ошибка при обновлении: " + e.getMessage(), e);
    }
  }

  // e. Удалить по ID
  @Override
  public boolean deleteById(String id) {
    String sql = "DELETE FROM courses.teachers WHERE staff_number=?";
    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      stmt.setString(1, id);
      return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при удалении: " + e.getMessage(), e);
    }
  }

  // f. Получить количество
  @Override
  public int getCount() {
    String sql = "SELECT COUNT(*) FROM courses.teachers";
    try (Connection conn = getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
      if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при подсчёте записей: " + e.getMessage(), e);
    }
    return 0;
  }

  // 🔧 Приватный метод для сбора объекта из ResultSet
  private Teacher extractTeacher(ResultSet rs) throws SQLException {
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

  @Override
  public void readAll() {
    throw new UnsupportedOperationException("Не используется в БД");
  }

  @Override
  public void writeAll() {
    throw new UnsupportedOperationException("Не используется в БД");
  }
}
