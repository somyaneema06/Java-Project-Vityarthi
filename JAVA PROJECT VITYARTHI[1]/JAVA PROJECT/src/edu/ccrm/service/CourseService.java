package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course addCourse(Course c);
    Optional<Course> findByCode(String code);
    Collection<Course> listAll();
    List<Course> searchByInstructor(String instructorId);
    List<Course> filterByDept(String dept);
    List<Course> filterBySemester(Semester s);
}
