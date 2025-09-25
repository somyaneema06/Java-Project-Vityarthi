package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Course service demonstrating usage of Streams and lambdas for filters/search.
 */
public class CourseServiceImpl implements CourseService {
    private final DataStore ds = DataStore.getInstance();

    @Override
    public Course addCourse(Course c) {
        ds.courses().put(c.getCode().getCode(), c);
        return c;
    }

    @Override
    public Optional<Course> findByCode(String code) {
        return Optional.ofNullable(ds.courses().get(code.toUpperCase()));
    }

    @Override
    public Collection<Course> listAll() {
        return ds.courses().values();
    }

    @Override
    public List<Course> searchByInstructor(String instructorId) {
        return ds.courses().values().stream()
                .filter(c -> instructorId.equals(c.getInstructorId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> filterByDept(String dept) {
        return ds.courses().values().stream()
                .filter(c -> c.getDepartment().equalsIgnoreCase(dept))
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> filterBySemester(Semester s) {
        return ds.courses().values().stream()
                .filter(c -> c.getSemester() == s)
                .collect(Collectors.toList());
    }
}
