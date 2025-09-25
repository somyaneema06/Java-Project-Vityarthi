package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.exceptions.DuplicateEnrollmentException;
import edu.ccrm.exceptions.MaxCreditLimitExceededException;

import java.util.Optional;

/**
 * Enrollment service handles business rules.
 * Demonstrates custom exceptions and simple credit limit enforcement.
 */
public class EnrollmentServiceImpl implements EnrollmentService {
    private final DataStore ds = DataStore.getInstance();
    private final AppConfigWrapper cfg = new AppConfigWrapper();

    @Override
    public Enrollment enroll(String studentId, String courseCode) throws DuplicateEnrollmentException {
        Student s = ds.students().get(studentId);
        Course c = ds.courses().get(courseCode.toUpperCase());
        if (s == null) throw new IllegalArgumentException("Student not found");
        if (c == null) throw new IllegalArgumentException("Course not found");
        // duplicate check
        if (s.getEnrollments().stream().anyMatch(e -> e.getCourse().getCode().getCode().equalsIgnoreCase(courseCode))) {
            throw new DuplicateEnrollmentException("Already enrolled");
        }
        // simple credit sum
        int total = s.getEnrollments().stream().mapToInt(e -> e.getCourse().getCredits()).sum();
        if (total + c.getCredits() > cfg.getMaxCredits()) {
            throw new MaxCreditLimitExceededException("Max credits exceeded");
        }
        Enrollment en = new Enrollment(studentId, c);
        s.addEnrollment(en);
        return en;
    }

    @Override
    public void unenroll(String studentId, String courseCode) {
        Student s = ds.students().get(studentId);
        if (s != null) {
            s.removeEnrollment(courseCode.toUpperCase());
        }
    }

    @Override
    public void recordMarks(String studentId, String courseCode, double marks) {
        Student s = ds.students().get(studentId);
        if (s == null) throw new IllegalArgumentException("No student");
        s.getEnrollments().stream()
                .filter(e -> e.getCourse().getCode().getCode().equalsIgnoreCase(courseCode))
                .findFirst()
                .ifPresent(e -> e.setMarks(marks));
    }

    @Override
    public Optional<Enrollment> find(String studentId, String courseCode) {
        Student s = ds.students().get(studentId);
        if (s == null) return Optional.empty();
        return s.getEnrollments().stream().filter(e -> e.getCourse().getCode().getCode().equalsIgnoreCase(courseCode)).findFirst();
    }

    @Override
    public double computeGPA(String studentId) {
        Student s = ds.students().get(studentId);
        if (s == null) return 0.0;
        return s.getEnrollments().stream()
                .filter(e -> e.getGrade().isPresent())
                .mapToDouble(e -> e.getGrade().get().getPoints() * e.getCourse().getCredits())
                .sum() / Math.max(1, s.getEnrollments().stream().filter(e -> e.getGrade().isPresent()).mapToInt(e -> e.getCourse().getCredits()).sum());
    }

    // small wrapper for config to avoid direct dependency in this class file
    static class AppConfigWrapper {
        public int getMaxCredits() {
            return edu.ccrm.config.AppConfig.getInstance().getMaxCreditsPerSemester();
        }
    }
}
