package ru.yourorg.cources.db;

import ru.yourorg.cources.util.EnvConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
  private static DBManager instance;
  private Connection connection;

  private DBManager() {
    try {
      String url = EnvConfig.getDbUrl();
      String user = EnvConfig.getDbUser();
      String password = EnvConfig.getDbPassword();
      connection = DriverManager.getConnection(url, user, password);
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
