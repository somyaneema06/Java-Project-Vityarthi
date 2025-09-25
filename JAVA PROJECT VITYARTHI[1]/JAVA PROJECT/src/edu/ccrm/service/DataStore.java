package edu.ccrm.service;

import edu.ccrm.domain.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory datastore. Demonstrates Singleton-like shared store usage.
 */
public class DataStore {
    private static final DataStore INSTANCE = new DataStore();

    private final Map<String, Student> students = new ConcurrentHashMap<>();
    private final Map<String, Course> courses = new ConcurrentHashMap<>();
    private final Map<String, Instructor> instructors = new ConcurrentHashMap<>();

    private DataStore() {}

    public static DataStore getInstance() { return INSTANCE; }

    public Map<String, Student> students() { return students; }
    public Map<String, Course> courses() { return courses; }
    public Map<String, Instructor> instructors() { return instructors; }
}
