package org.example.cli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.model.Student;

import java.util.List;

class StudentFormatterTest {
    private StudentFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new StudentFormatter();
    }

    @Test
    void format_shouldReturnStudentWithoutGrades() {
        Student student = new Student(1, "Alex");
        String result = formatter.format(student);
        String expected = "ID: 1 | Name: Alex | Average: 0.0 | Grades: []";

        assertEquals(expected, result);
    }

    @Test
    void format_shouldReturnStudentWithGrades() {
        Student student = new Student(1, "Alex", List.of(3, 4, 5));
        String result = formatter.format(student);
        String expected = "ID: 1 | Name: Alex | Average: 4.0 | Grades: [3, 4, 5]";

        assertEquals(expected, result);
    }

    @Test
    void format_shouldThrowExceptionWhenStudentIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> formatter.format(null));
    }
}
