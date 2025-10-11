package ru.yourorg.cources.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
  private static DBManager instance;
  private Connection connection;

  private static final String URL = "jdbc:postgresql://localhost:5432/courses";
  private static final String USER = "postgres";
  private static final String PASSWORD = "admin"; // замени на свой пароль

  private DBManager() {
    try {
      connection = DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка подключения к базе данных", e);
    }
  }

  public static DBManager getInstance() {
    if (instance == null) {
      synchronized (DBManager.class) {
        if (instance == null) {
          instance = new DBManager();
        }
      }
    }
    return instance;
  }

  public Connection getConnection() {
    return connection;
  }
}
