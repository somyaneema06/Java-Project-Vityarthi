package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.service.DataStore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Demonstrates NIO.2 + Streams usage for import/export.
 * Supports a simple CSV-like format for students and courses.
 */
public class ImportExportService {
    private final DataStore ds = DataStore.getInstance();

    public Path exportStudents(Path folder) throws IOException {
        Files.createDirectories(folder);
        Path out = folder.resolve("students.csv");
        List<String> lines = ds.students().values().stream()
                .map(s -> String.join(",", s.getId(), s.getRegNo(), s.getFullName(), s.getEmail(), s.getDob().toString(), String.valueOf(s.isActive())))
                .collect(Collectors.toList());
        Files.write(out, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return out;
    }

    public Path exportCourses(Path folder) throws IOException {
        Files.createDirectories(folder);
        Path out = folder.resolve("courses.csv");
        List<String> lines = ds.courses().values().stream()
                .map(c -> String.join(",", c.getCode().getCode(), c.getTitle(), String.valueOf(c.getCredits()), String.valueOf(c.getSemester()), c.getDepartment(), String.valueOf(c.getInstructorId())))
                .collect(Collectors.toList());
        Files.write(out, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return out;
    }

    public void importStudents(Path inFile) throws IOException {
        try (Stream<String> s = Files.lines(inFile)) {
            s.forEach(line -> {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String id = parts[0];
                    String regNo = parts[1];
                    String fullname = parts[2];
                    String email = parts[3];
                    LocalDate dob = LocalDate.parse(parts[4]);
                    boolean active = Boolean.parseBoolean(parts[5]);
                    Student st = new Student(id, regNo, fullname, email, dob);
                    if (!active) st.deactivate();
                    ds.students().put(id, st);
                }
            });
        }
    }

    public void importCourses(Path inFile) throws IOException {
        try (Stream<String> s = Files.lines(inFile)) {
            s.forEach(line -> {
                String[] p = line.split(",");
                if (p.length >= 6) {
                    Course c = new Course.Builder(p[0])
                            .title(p[1])
                            .credits(Integer.parseInt(p[2]))
                            .semester(Semester.valueOf(p[3]))
                            .department(p[4])
                            .instructorId(p[5])
                            .build();
                    ds.courses().put(c.getCode().getCode(), c);
                }
            });
        }
    }
}
