package ru.yourorg.cources.model;

import ru.yourorg.cources.util.Validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Teacher extends Person {

    private final String staffNumber;
    private final LocalDate employmentStart;
    private final int experienceYears;
    private final String qualification;
    private final String notes;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Teacher(String staffNumber, String lastName, String firstName, String patronymic,
                    String phone, String email, LocalDate employmentStart, int experienceYears,
                    String qualification, String notes) {
        super(lastName, firstName, patronymic, phone, email);
        this.staffNumber = Validators.requireNonEmpty(staffNumber, "Табельный номер");
        this.employmentStart = Validators.requireNotFuture(employmentStart, "Дата начала работы");
        this.experienceYears = Validators.requireNonNegative(experienceYears, "Стаж работы");
        this.qualification = qualification != null ? qualification.trim() : null;
        this.notes = notes != null ? notes.trim() : null;
    }

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

    // ================= JSON =================
    public static Teacher fromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON строка пуста");
        }
        String body = json.trim();
        if (body.startsWith("{")) body = body.substring(1);
        if (body.endsWith("}")) body = body.substring(0, body.length() - 1);

        String[] entries = body.split(",");
        String staffNumber = null, lastName = null, firstName = null, patronymic = null,
                phone = null, email = null, qualification = null, notes = null;
        LocalDate employmentStart = null;
        int experienceYears = 0;

        for (String entry : entries) {
            String[] kv = entry.split(":", 2);
            if (kv.length != 2) continue;
            String key = kv[0].trim().replaceAll("\"", "");
            String value = kv[1].trim().replaceAll("\"", "");

            switch (key) {
                case "staffNumber" -> staffNumber = value;
                case "lastName" -> lastName = value;
                case "firstName" -> firstName = value;
                case "patronymic" -> patronymic = value.isEmpty() ? null : value;
                case "phone" -> phone = value.isEmpty() ? null : value;
                case "email" -> email = value.isEmpty() ? null : value;
                case "employmentStart" -> employmentStart = value.isEmpty() ? null : LocalDate.parse(value, DATE_FORMAT);
                case "experienceYears" -> experienceYears = value.isEmpty() ? 0 : Integer.parseInt(value);
                case "qualification" -> qualification = value.isEmpty() ? null : value;
                case "notes" -> notes = value.isEmpty() ? null : value;
            }
        }

        return Teacher.of(staffNumber, lastName, firstName, patronymic,
                phone, email, employmentStart, experienceYears, qualification, notes);
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
};
