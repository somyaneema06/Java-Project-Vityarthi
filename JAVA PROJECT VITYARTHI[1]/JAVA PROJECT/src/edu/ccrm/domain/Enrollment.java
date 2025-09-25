package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Enrollment ties Student -> Course with optional marks/grade.
 */
public class Enrollment {
    private final String studentId;
    private final Course course;
    private final LocalDate enrolledAt;
    private Double marks; // raw marks
    private Grade grade;

    public Enrollment(String studentId, Course course) {
        this.studentId = studentId;
        this.course = course;
        this.enrolledAt = LocalDate.now();
    }

    public String getStudentId() { return studentId; }
    public Course getCourse() { return course; }
    public LocalDate getEnrolledAt() { return enrolledAt; }

    public Optional<Double> getMarks() { return marks == null ? Optional.empty() : Optional.of(marks); }
    public Optional<Grade> getGrade() { return grade == null ? Optional.empty() : Optional.of(grade); }

    public void setMarks(double m) {
        this.marks = m;
        this.grade = computeLetterGrade(m);
    }

    private Grade computeLetterGrade(double m) {
        if (m >= 90) return Grade.S;
        if (m >= 80) return Grade.A;
        if (m >= 70) return Grade.B;
        if (m >= 60) return Grade.C;
        if (m >= 50) return Grade.D;
        if (m >= 40) return Grade.E;
        return Grade.F;
    }

    @Override
    public String toString() {
        return "Enrollment[studentId=" + studentId + ", course=" + course.getCode() + ", enrolledAt=" + enrolledAt + ", marks=" + marks + ", grade=" + grade + "]";
    }
}
