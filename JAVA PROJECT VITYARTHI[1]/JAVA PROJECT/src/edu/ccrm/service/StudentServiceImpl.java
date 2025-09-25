package edu.ccrm.service;

import edu.ccrm.domain.Student;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Simple implementation demonstrating basic operations.
 */
public class StudentServiceImpl implements StudentService {
    private final DataStore ds = DataStore.getInstance();

    @Override
    public Student addStudent(Student s) {
        String id = s.getId();
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Student id required");
        }
        ds.students().put(id, s);
        return s;
    }

    @Override
    public Optional<Student> findById(String id) {
        return Optional.ofNullable(ds.students().get(id));
    }

    @Override
    public Collection<Student> listAll() {
        return ds.students().values();
    }

    @Override
    public void deactivate(String id) {
        Student s = ds.students().get(id);
        if (s != null) s.deactivate();
    }
}
