package edu.ccrm.domain;

/**
 * Immutable CourseCode demonstrating immutability.
 */
public final class CourseCode {
    private final String code;

    public CourseCode(String code) {
        if (code == null) throw new IllegalArgumentException("code null");
        this.code = code.toUpperCase();
    }

    public String getCode() { return code; }

    @Override
    public String toString() { return code; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseCode)) return false;
        CourseCode cc = (CourseCode) o;
        return code.equals(cc.code);
    }

    @Override
    public int hashCode() { return code.hashCode(); }
}
