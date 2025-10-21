package ru.yourorg.cources.util.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonObjectMapperProvider {

  private static final ObjectMapper mapper;

  static {
    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  public static ObjectMapper getMapper() {
    return mapper;
  }
}
