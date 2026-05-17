package org.example.model;

import org.junit.jupiter.api.*;
import org.example.exception.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
  @Test
  void constuctor_shouldIdCreateStudentWithValidData() {
    // Arrange + public
    Student student = new Student(1, "Alex");

    assertEquals(1, student.getId());
    assertEquals("Alex", student.getName());
    assertTrue(student.getGrades().isEmpty());
  }

  @Test
  void constuctor_shouldThrowExceptionWhenIdNotPositive() {
    // Act + Assert
    assertThrows(
        IllegalArgumentException.class,
        () -> new Student(0, "Alex"));
  }

  @Test
  void constuctor_shouldThrowExceptionWhenNameIsBlank() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Student(2, " "));
  }

  @Test
  void addGrade_souldAndValidGrade() {
    // Arrange
    Student student = new Student(1, "Alex");

    // Act
    student.addGrade(5);

    // Assert
    assertEquals(List.of(5), student.getGrades());
  }

  @Test
  void addGrade_shoutldThrowExceptionWhenGradeIsTooHigh() {
    Student student = new Student(1, "Alex");

    assertThrows(
        InvalidGradeException.class,
        () -> student.addGrade(6));
  }

  @Test
  void addGrade_shoutldThrowExceptionWhenGradeIsTooLow() {
    Student student = new Student(1, "Alex");

    assertThrows(
        InvalidGradeException.class,
        () -> student.addGrade(0));
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
  void studentWithSameId_shouldBeEquals() {
    Student first = new Student(1, "Alex");
    Student second = new Student(1, "Bob");

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
  }

  @Test
  void getGrades_shouldReturnUnmodifiableList() {
    Student student = new Student(1, "Alex");
    student.addGrade(5);

    assertThrows(
        UnsupportedOperationException.class,
        () -> student.getGrades().add(4));
  }
}
