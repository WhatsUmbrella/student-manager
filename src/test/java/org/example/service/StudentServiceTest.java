package org.example.service;

import org.example.model.Student;
import org.example.repository.StudentRepository;
import org.example.exception.StudentNotFoundException;
import org.example.exception.InvalidGradeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
  private StudentRepository studentRepository;
  private StudentService service;

  @BeforeEach
  void setUp() {
    studentRepository = new StudentRepository();
    service = new StudentService(studentRepository);
  }

  @Test
  void addStudent_shouldFindStudentAfterAdded() {
    // Arrange
    Student student = new Student(1, "Alex");

    service.addStudent(student);

    assertEquals(student, service.findStudentById(1));
  }

  @Test
  void findStudentById_shouldReturnStudentWhenStudentExist() {
    Student student = new Student(1, "Alex");

    service.addStudent(student);

    assertEquals(student, service.findStudentById(1));
  }

  @Test
  void findStudentById_shouldThrowExceptionWhenStudentDoesNotExist() {
    assertThrows(
        StudentNotFoundException.class,
        () -> service.findStudentById(200));

  }

  @Test
  void deleteStudentById_shouldRemovedStudentWhenStudentExist() {
    Student student = new Student(1, "Alex");

    service.addStudent(student);
    service.deleteStudentById(1);

    assertThrows(
        StudentNotFoundException.class,
        () -> service.findStudentById(1));
  }

  @Test
  void deleteStudentById_shouldThrowExceptionWhenStudentDoesNotExist() {

    assertThrows(
        StudentNotFoundException.class,
        () -> service.deleteStudentById(100));
  }

  @Test
  void addGradeToStudent_shouldAddGradeWhenStudentExistAndGradeValid() {
    Student student = new Student(1, "Alex");

    service.addStudent(student);
    service.addGradeToStudent(1, 3);

    assertEquals(List.of(3), service.findStudentById(1).getGrades());
  }

  @Test
  void addGradeToStudent_shouldThowExceptionWhenStudentDoesNotExist() {
    Student student = new Student(1, "Alex");
    service.addStudent(student);

    assertThrows(
        StudentNotFoundException.class,
        () -> service.addGradeToStudent(2, 3));
  }

  @Test
  void addGradeToStudent_shouldThowExceptionWhenGradeInvalid() {
    Student student = new Student(1, "Alex");

    service.addStudent(student);

    assertThrows(
        InvalidGradeException.class,
        () -> service.addGradeToStudent(1, 6));
  }

  @Test
  void getAverageGrade_shouldReturnValidAverageGradeWhenStudentExist() {
    Student student = new Student(1, "Alex");

    service.addStudent(student);
    service.addGradeToStudent(1, 3);
    service.addGradeToStudent(1, 4);
    service.addGradeToStudent(1, 5);

    double average = service.getAverageGrade(1);
    assertEquals(4.0, average);
  }

  @Test
  void getAverageGrade_shouldThowExceptionWhenStudentDoesNotExist() {
    assertThrows(
        StudentNotFoundException.class,
        () -> service.getAverageGrade(2));
  }
}
