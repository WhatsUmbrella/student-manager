package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.example.exception.InvalidGradeException;

public class Student {
    private final int id;
    private String name;
    private final List<Integer> grades;

    public Student(int id, String name) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive.");
        }
        this.id = id;
        setName(name);
        grades = new ArrayList<>();
    }

    public Student(int id, String name, List<Integer> grades) {
        this(id, name);
        if (grades == null) {
            throw new IllegalArgumentException("Grades can't be null.");
        }
        for (int grade : grades) {
            addGrade(grade);
        }
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can't be empty.");
        }
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getGrades() {
        return Collections.unmodifiableList(grades);
    }

    public void addGrade(int grade) {
        if (grade < 1 || grade > 5) {
            throw new InvalidGradeException(grade);
        }
        grades.add(grade);
    }

    public double getAverageGrade() {
        return grades.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    public boolean hasGrades() {
        return !grades.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student student)) {
            return false;
        }
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
