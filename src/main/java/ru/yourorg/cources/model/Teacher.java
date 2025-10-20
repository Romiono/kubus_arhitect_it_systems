package ru.yourorg.cources.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.yourorg.cources.util.Validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Teacher extends Person {

  private final String staffNumber;
  private final LocalDate employmentStart;
  private final int experienceYears;
  private final String qualification;
  private final String notes;

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @JsonCreator
  public Teacher(
    @JsonProperty("staffNumber") String staffNumber,
    @JsonProperty("lastName") String lastName,
    @JsonProperty("firstName") String firstName,
    @JsonProperty("patronymic") String patronymic,
    @JsonProperty("phone") String phone,
    @JsonProperty("email") String email,
    @JsonProperty("employmentStart") LocalDate employmentStart,
    @JsonProperty("experienceYears") int experienceYears,
    @JsonProperty("qualification") String qualification,
    @JsonProperty("notes") String notes
  ) {
    super(lastName, firstName, patronymic, phone, email);
    this.staffNumber = Validators.requireNonEmpty(staffNumber, "Табельный номер");
    this.employmentStart = Validators.requireNotFuture(employmentStart, "Дата начала работы");
    this.experienceYears = Validators.requireNonNegative(experienceYears, "Стаж работы");
    this.qualification = qualification != null ? qualification.trim() : null;
    this.notes = notes != null ? notes.trim() : null;
  }

  // Фабричный метод (необязательно, но удобно)
  public static Teacher of(String staffNumber, String lastName, String firstName, String patronymic,
                           String phone, String email, LocalDate employmentStart, int experienceYears,
                           String qualification, String notes) {
    return new Teacher(staffNumber, lastName, firstName, patronymic, phone, email,
      employmentStart, experienceYears, qualification, notes);
  }

  // ================= CSV =================
  public static Teacher fromCsv(String csvLine) {
    if (csvLine == null || csvLine.trim().isEmpty()) {
      throw new IllegalArgumentException("CSV строка пуста");
    }
    String[] parts = csvLine.split(",", -1);
    if (parts.length != 10) {
      throw new IllegalArgumentException("CSV строка должна содержать 10 полей");
    }
    return Teacher.of(
      parts[0].trim(),
      parts[1].trim(),
      parts[2].trim(),
      parts[3].trim().isEmpty() ? null : parts[3].trim(),
      parts[4].trim().isEmpty() ? null : parts[4].trim(),
      parts[5].trim().isEmpty() ? null : parts[5].trim(),
      parts[6].trim().isEmpty() ? null : LocalDate.parse(parts[6].trim(), DATE_FORMAT),
      parts[7].trim().isEmpty() ? 0 : Integer.parseInt(parts[7].trim()),
      parts[8].trim().isEmpty() ? null : parts[8].trim(),
      parts[9].trim().isEmpty() ? null : parts[9].trim()
    );
  }

  // ================= Геттеры =================
  public String getStaffNumber() {
    return staffNumber;
  }

  public LocalDate getEmploymentStart() {
    return employmentStart;
  }

  public int getExperienceYears() {
    return experienceYears;
  }

  public String getQualification() {
    return qualification;
  }

  public String getNotes() {
    return notes;
  }

  public String toStringFull() {
    return "Teacher{" +
      "staffNumber='" + staffNumber + '\'' +
      ", " + super.toString() +
      ", employmentStart=" + employmentStart +
      ", experienceYears=" + experienceYears +
      ", qualification='" + qualification + '\'' +
      ", notes='" + notes + '\'' +
      '}';
  }
}
