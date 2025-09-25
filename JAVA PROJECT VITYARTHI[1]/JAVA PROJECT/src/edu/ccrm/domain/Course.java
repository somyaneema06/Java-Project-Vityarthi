package edu.ccrm.domain;

import java.util.Objects;

/**
 * Course domain object. Demonstrates Builder pattern and static nested Builder class.
 */
public class Course {
    private final CourseCode code;
    private String title;
    private int credits;
    private String instructorId;
    private Semester semester;
    private String department;
    private boolean active = true;

    private Course(Builder b) {
        this.code = b.code;
        this.title = b.title;
        this.credits = b.credits;
        this.instructorId = b.instructorId;
        this.semester = b.semester;
        this.department = b.department;
    }

    public CourseCode getCode() { return code; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getInstructorId() { return instructorId; }
    public Semester getSemester() { return semester; }
    public String getDepartment() { return department; }
    public boolean isActive() { return active; }
    public void deactivate() { this.active = false; }

    @Override
    public String toString() {
        return "Course{code=" + code + ", title=" + title + ", credits=" + credits + ", instructorId=" + instructorId + ", semester=" + semester + ", dept=" + department + "}";
    }

    // Builder (static nested class)
    public static class Builder {
        private final CourseCode code;
        private String title = "";
        private int credits = 3;
        private String instructorId = null;
        private Semester semester = Semester.FALL;
        private String department = "General";

        public Builder(String code) {
            this.code = new CourseCode(code);
        }
        public Builder title(String t) { this.title = t; return this; }
        public Builder credits(int c) { this.credits = c; return this; }
        public Builder instructorId(String id) { this.instructorId = id; return this; }
        public Builder semester(Semester s) { this.semester = s; return this; }
        public Builder department(String d) { this.department = d; return this; }
        public Course build() {
            Objects.requireNonNull(code);
            return new Course(this);
        }
    }
}
