package edu.ccrm.service;

import edu.ccrm.domain.Student;

import java.util.Collection;
import java.util.Optional;

public interface StudentService {
    Student addStudent(Student s);
    Optional<Student> findById(String id);
    Collection<Student> listAll();
    void deactivate(String id);
}
