package edu.ccrm.domain;

import java.time.LocalDate;

/**
 * Abstract class demonstrating Abstraction & inheritance.
 */
public abstract class Person {
    protected final String id;
    protected String fullName;
    protected String email;
    protected LocalDate createdAt;

    public Person(String id, String fullName, String email) {
        assert id != null : "id must not be null";
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.createdAt = LocalDate.now();
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public abstract String getRole();

    @Override
    public String toString() {
        return getRole() + "{id=" + id + ", name=" + fullName + ", email=" + email + ", created=" + createdAt + "}";
    }
}
