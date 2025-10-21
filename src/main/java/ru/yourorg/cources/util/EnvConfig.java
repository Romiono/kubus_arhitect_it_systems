package ru.yourorg.cources.util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {

  private static final Dotenv dotenv;

  static {
    try {
      dotenv = Dotenv.configure()
          .directory(".")
          .ignoreIfMissing()
          .load();
    } catch (Exception e) {
      throw new RuntimeException("Ошибка загрузки .env файла: " + e.getMessage(), e);
    }
  }

  public static String get(String key) {
    String value = dotenv.get(key);
    if (value == null) {
      value = System.getenv(key);
    }
    return value;
  }

  public static String get(String key, String defaultValue) {
    String value = get(key);
    return value != null ? value : defaultValue;
  }

  // Database configuration
  public static String getDbUrl() {
    return get("DB_URL", "jdbc:postgresql://localhost:5432/OOP");
  }

  public static String getDbUser() {
    return get("DB_USER", "postgres");
  }

  public static String getDbPassword() {
    return get("DB_PASSWORD", "");
  }

  // File paths configuration
  public static String getDataDir() {
    return get("DATA_DIR", ".");
  }

  public static String getJsonFile() {
    return get("JSON_FILE", "teachers_test.json");
  }

  public static String getYamlFile() {
    return get("YAML_FILE", "teachers_test.yaml");
  }

  public static String getDecoratorJsonFile() {
    return get("DECORATOR_JSON_FILE", "teachers_decorator_test.json");
  }
}
