package edu.ccrm.service;

import edu.ccrm.domain.Enrollment;
import edu.ccrm.exceptions.DuplicateEnrollmentException;

import java.util.Optional;

public interface EnrollmentService {
    Enrollment enroll(String studentId, String courseCode) throws DuplicateEnrollmentException;
    void unenroll(String studentId, String courseCode);
    void recordMarks(String studentId, String courseCode, double marks);
    Optional<Enrollment> find(String studentId, String courseCode);
    double computeGPA(String studentId);
}
