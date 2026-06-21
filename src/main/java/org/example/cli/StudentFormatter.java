package org.example.cli;

import org.example.model.Student;

import java.util.List;
import java.util.stream.Collectors;

public class StudentFormatter {
    public String format(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student can't be null.");
        }

        String grades;
        List<Integer> gradesList = student.getGrades();

        if (gradesList.isEmpty()) {
            grades = "";
        } else {
            grades = gradesList.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
        }

        return String.format("ID: %d | Name: %s | Average: %.1f | Grades: [%s]",
                student.getId(), student.getName(), student.getAverageGrade(), grades);
    }
}
