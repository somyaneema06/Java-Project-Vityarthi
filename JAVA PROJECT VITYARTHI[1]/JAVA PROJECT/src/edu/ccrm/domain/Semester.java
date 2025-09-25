package edu.ccrm.domain;

/**
 * Enum demonstrating enums with constructors and fields.
 */
public enum Semester {
    SPRING("Spring"),
    SUMMER("Summer"),
    FALL("Fall");

    private final String display;
    Semester(String display) { this.display = display; }
    public String display() { return display; }
    @Override public String toString() { return display; }
}
