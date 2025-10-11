package ru.yourorg.cources.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonObjectMapperProvider {

  private static final ObjectMapper mapper;

  static {
    mapper = new ObjectMapper();
    // Добавляем модуль для поддержки Java 8 time API (LocalDate, LocalDateTime и т.п.)
    mapper.registerModule(new JavaTimeModule());

    // Отключаем запись дат как timestamps (иначе будут числа вместо "yyyy-MM-dd")
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public static ObjectMapper getMapper() {
    return mapper;
  }
}
