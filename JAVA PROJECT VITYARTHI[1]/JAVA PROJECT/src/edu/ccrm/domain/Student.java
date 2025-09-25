package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.*;

/**
 * Student domain object. Encapsulation: private fields + getters/setters.
 * Demonstrates composition (list of enrollments), polymorphism via toString.
 */
public class Student extends Person {
    private final String regNo;
    private boolean active;
    private final LocalDate dob;
    private final Map<String, Enrollment> enrollments = new HashMap<>(); // key: courseCode

    public Student(String id, String regNo, String fullName, String email, LocalDate dob) {
        super(id, fullName, email);
        this.regNo = regNo;
        this.dob = dob;
        this.active = true;
    }

    public String getRegNo() { return regNo; }
    public boolean isActive() { return active; }
    public void deactivate() { this.active = false; }
    public LocalDate getDob() { return dob; }

    public Collection<Enrollment> getEnrollments() {
        return Collections.unmodifiableCollection(enrollments.values());
    }

    public void addEnrollment(Enrollment e) {
        enrollments.put(e.getCourse().getCode().getCode(), e);
    }

    public void removeEnrollment(String courseCode) {
        enrollments.remove(courseCode);
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public String toString() {
        return super.toString() + " regNo=" + regNo + " active=" + active + " enrollCount=" + enrollments.size();
    }
}
