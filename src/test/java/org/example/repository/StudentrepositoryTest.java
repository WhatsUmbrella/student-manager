package org.example.repository;

import org.example.model.Student;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {

  @Test
  void save_shouldSaveStudent() {
    // Arrange
    StudentRepository repository = new StudentRepository();
    Student student = new Student(1, "Alex");

    // Act
    repository.save(student);

    // Assert
    Optional<Student> result = repository.findById(1);
    assertTrue(result.isPresent());
    assertEquals(student, result.get());
  }

  @Test
  void findById_shouldReturnEmptyOptionalWhenStudentDoesNotExist() {
    // Arrange
    StudentRepository repository = new StudentRepository();

    // Act
    Optional<Student> result = repository.findById(999);

    // Assert
    assertTrue(result.isEmpty());
  }

  @Test
  void findAll_shouldReturnAllStudents() {
    // Arrange
    StudentRepository repository = new StudentRepository();
    Student alex = new Student(1, "Alex");
    Student bob = new Student(2, "Bob");

    repository.save(alex);
    repository.save(bob);

    // Act
    List<Student> students = repository.findAll();

    // Assert
    assertEquals(2, students.size());
    assertTrue(students.contains(alex));
    assertTrue(students.contains(bob));
  }

  @Test
  void existsById_shouldReturnTrueWhenStudentExists() {
    // Arrange
    StudentRepository repository = new StudentRepository();
    repository.save(new Student(1, "Alex"));

    // Act
    boolean exists = repository.existsById(1);

    // Assert
    assertTrue(exists);
  }

  @Test
  void existsById_shouldReturnFalseWhenStudentDoesNotExist() {
    // Arrange
    StudentRepository repository = new StudentRepository();

    // Act
    boolean exists = repository.existsById(1);

    // Assert
    assertFalse(exists);
  }

  @Test
  void deleteById_shouldDeleteStudent() {
    // Arrange
    StudentRepository repository = new StudentRepository();
    repository.save(new Student(1, "Alex"));

    // Act
    repository.deleteById(1);

    // Assert
    assertTrue(repository.findById(1).isEmpty());
  }

  @Test
  void save_shouldReplaceStudentWithSameId() {
    // Arrange
    StudentRepository repository = new StudentRepository();
    Student oldStudent = new Student(1, "Alex");
    Student newStudent = new Student(1, "Alexander");

    repository.save(oldStudent);

    // Act
    repository.save(newStudent);

    // Assert
    List<Student> students = repository.findAll();
    assertEquals(1, students.size());
    assertEquals("Alexander", repository.findById(1).get().getName());
  }

  @Test
  void findAll_shouldReturnUnmodifiableList() {
    // Arrange
    StudentRepository repository = new StudentRepository();
    repository.save(new Student(1, "Alex"));

    // Act
    List<Student> students = repository.findAll();

    // Assert
    assertThrows(
        UnsupportedOperationException.class,
        () -> students.add(new Student(2, "Bob")));
  }

  @Test
  void count_shouldReturnCountOfStudent() {
    StudentRepository repository = new StudentRepository();
    repository.save(new Student(1, "Alex"));
    repository.save(new Student(2, "Bob"));

    int countOfStudent = repository.count();

    assertEquals(2, countOfStudent);
  }
}
