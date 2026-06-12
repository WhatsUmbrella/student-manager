package org.example.model;

import org.junit.jupiter.api.Test;
import org.example.exception.InvalidGradeException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    @Test
    void constructor_shouldCreateStudentWithValidData() {
        Student student = new Student(1, "Alex");

        assertEquals(1, student.getId());
        assertEquals("Alex", student.getName());
        assertTrue(student.getGrades().isEmpty());
    }

    @Test
    void constructor_shouldThrowExceptionWhenIdNotPositive() {
        assertThrows(IllegalArgumentException.class, () -> new Student(0, "Alex"));
    }

    @Test
    void constructor_shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Student(2, " "));
    }

    @Test
    void constructor_shouldThrowExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new Student(2, null));
    }

    @Test
    void constructor_shouldCreateStudentWithGrades() {
        Student student = new Student(1, "Alex", List.of(3, 4, 5));
        assertEquals(1, student.getId());
        assertEquals("Alex", student.getName());
        assertEquals(List.of(3, 4, 5), student.getGrades());
    }

    @Test
    void constructor_shouldThrowExceptionWhenGradesContainInvalidGrade() {
        assertThrows(
                InvalidGradeException.class,
                () -> new Student(2, "Alex", List.of(4, 6, 4)));
    }

    @Test
    void constructor_shouldThrowExceptionWhenGradesIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Student(2, "Alex", null));
    }

    @Test
    void addGrade_shouldAddValidGrade() {
        Student student = new Student(1, "Alex");

        student.addGrade(5);

        assertEquals(List.of(5), student.getGrades());
    }

    @Test
    void addGrade_shouldThrowExceptionWhenGradeIsTooHigh() {
        Student student = new Student(1, "Alex");

        assertThrows(InvalidGradeException.class, () -> student.addGrade(6));
    }

    @Test
    void addGrade_shouldThrowExceptionWhenGradeIsTooLow() {
        Student student = new Student(1, "Alex");

        assertThrows(InvalidGradeException.class, () -> student.addGrade(0));
    }

    @Test
    void getAverageGrade_shouldReturnAverageWhenStudentHasGrades() {
        Student student = new Student(1, "Alex");
        student.addGrade(5);
        student.addGrade(4);
        student.addGrade(3);

        double average = student.getAverageGrade();

        assertEquals(4.0, average);
    }

    @Test
    void getAverageGrade_shouldReturnZeroWhenStudentHasNoGrades() {
        Student student = new Student(1, "Alex");

        double average = student.getAverageGrade();

        assertEquals(0.0, average);
    }

    @Test
    void studentsWithSameId_shouldBeEqual() {
        Student first = new Student(1, "Alex");
        Student second = new Student(1, "Bob");

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void studentsWithDifferentIds_shouldNotBeEqual() {
        Student first = new Student(1, "Alex");
        Student second = new Student(2, "Bob");

        assertNotEquals(first, second);
    }

    @Test
    void getGrades_shouldReturnUnmodifiableList() {
        Student student = new Student(1, "Alex");
        student.addGrade(5);

        assertThrows(UnsupportedOperationException.class, () -> student.getGrades().add(4));
    }

    @Test
    void hasGrades_shouldReturnFalseWhenStudentHasNoGrades() {
        Student alex = new Student(1, "Alex");

        assertFalse(alex.hasGrades());
    }

    @Test
    void hasGrades_shouldReturnTrueWhenStudentHasGrades() {
        Student alex = new Student(1, "Alex", List.of(3, 4, 4));

        assertTrue(alex.hasGrades());
    }
}
