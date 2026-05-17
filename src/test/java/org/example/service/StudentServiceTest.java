package org.example.service;

import org.example.model.Student;
import org.example.repository.StudentRepository;
import org.example.exception.StudentNotFoundException;
import org.example.exception.InvalidGradeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
  void deleteStudentById_shouldRemovedStudentWhenStudentExists() {
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
  void addGradeToStudent_shouldThrowExceptionWhenStudentDoesNotExist() {
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
  void getAverageGrade_shouldThrowExceptionWhenStudentDoesNotExist() {
    assertThrows(
        StudentNotFoundException.class,
        () -> service.getAverageGrade(2));
  }

  @Test
  void getAllStudents_shouldReturnAllAddedStudents() {
    Student student1 = new Student(1, "Alex");
    Student student2 = new Student(2, "Bob");
    Student student3 = new Student(3, "Carl");

    service.addStudent(student1);
    service.addStudent(student2);
    service.addStudent(student3);

    List<Student> students = service.getAllStudents();
    assertEquals(3, students.size());
    assertTrue(students.contains(student1));
    assertTrue(students.contains(student2));
    assertTrue(students.contains(student3));
  }

  @Test
  void getAllStudents_shouldReturnUnmodifierList() {
    List<Student> students = service.getAllStudents();

    assertThrows(
        UnsupportedOperationException.class,
        () -> students.add(new Student(10, "Georg")));
  }

  @Test
  void getAllStudents_shouldReturnEmptyListWhenStudentDoesNotAdded() {
    assertTrue(service.getAllStudents().isEmpty());
  }

  @Test
  void getStudentsWithAverageAbove_shouldReturnValidStudents() {
    Student student1 = new Student(1, "Alex", List.of(3, 4, 5));
    Student student2 = new Student(2, "Bob", List.of(4, 5, 5));
    Student student3 = new Student(3, "Carl", List.of(5, 5, 5));

    service.addStudent(student1);
    service.addStudent(student2);
    service.addStudent(student3);

    List<Student> students = service.getStudentsWithAverageAbove(4.5);

    assertFalse(students.contains(student1));
    assertTrue(students.contains(student2));
    assertTrue(students.contains(student3));
  }

  @Test
  void getStudentsWithAverageAbove_shouldReturnStudentsHaveAboveAverage() {
    Student student1 = new Student(1, "Alex", List.of(5, 4, 5));
    Student student2 = new Student(2, "Bob", List.of(4, 5));

    service.addStudent(student1);
    service.addStudent(student2);

    List<Student> students = service.getStudentsWithAverageAbove(4.5);

    assertTrue(students.contains(student1));
    assertFalse(students.contains(student2));
  }

  @Test
  void getStudentsWithAverageAbove_shouldReturnUnmodifierList() {
    List<Student> students = service.getStudentsWithAverageAbove(4.5);

    assertThrows(
        UnsupportedOperationException.class,
        () -> students.add(new Student(10, "Georg")));
  }

  @Test
  void getStudentsSortedByName_returnStudentsWhichSortedAscByName() {
    Student student1 = new Student(3, "Alex", List.of(3, 4, 5));
    Student student2 = new Student(2, "Bob", List.of(4, 5, 5));
    Student student3 = new Student(1, "Carl", List.of(5, 5, 5));

    service.addStudent(student3);
    service.addStudent(student2);
    service.addStudent(student1);

    List<Student> students = service.getStudentsSortedByName();

    List<String> names = students.stream()
        .map(Student::getName)
        .toList();

    assertEquals(List.of("Alex", "Bob", "Carl"), names);
  }

  @Test
  void getStudentsSortedByName_shouldReturnUnmodifierList() {
    List<Student> students = service.getStudentsSortedByName();

    assertThrows(
        UnsupportedOperationException.class,
        () -> students.add(new Student(10, "Georg")));
  }

  @Test
  void getStudentsSortedByAverageDesc_returnStudentsSortedByAverageDesc() {
    Student student1 = new Student(1, "Alex", List.of(3));
    Student student2 = new Student(2, "Bob", List.of(4));
    Student student3 = new Student(3, "Carl", List.of(5));
    Student student4 = new Student(4, "John", List.of(2));

    service.addStudent(student1);
    service.addStudent(student2);
    service.addStudent(student3);
    service.addStudent(student4);

    List<Student> students = service.getStudentsSortedByAverageDesc();

    List<Double> grades = students.stream()
        .map(Student::getAverageGrade)
        .toList();

    assertEquals(List.of(5.0, 4.0, 3.0, 2.0), grades);
  }

  @Test
  void getStudentsSortedByAverageDesc_returnStudentsSortedByNameAscWhenAverageEqual() {
    Student student1 = new Student(1, "Alex", List.of(3));
    Student student2 = new Student(2, "Bob", List.of(3));
    Student student3 = new Student(3, "Carl", List.of(5));

    service.addStudent(student1);
    service.addStudent(student2);
    service.addStudent(student3);

    List<Student> students = service.getStudentsSortedByAverageDesc();

    assertEquals(5.0, students.get(0).getAverageGrade());
    assertEquals("Alex", students.get(1).getName());
    assertEquals(3.0, students.get(1).getAverageGrade());
    assertEquals("Bob", students.get(2).getName());
    assertEquals(3.0, students.get(2).getAverageGrade());
  }

  @Test
  void getStudentsSortedByAverageDesc_shouldReturnUnmodifierList() {
    List<Student> students = service.getStudentsSortedByAverageDesc();

    assertThrows(
        UnsupportedOperationException.class,
        () -> students.add(new Student(10, "Georg")));
  }

  @Test
  void getStudentsMapById_shouldReturnMapWhereKeyIdValueStudentWithThisId() {
    Student student1 = new Student(1, "Alex", List.of(3));
    Student student2 = new Student(2, "Bob", List.of(4));
    Student student3 = new Student(3, "Carl", List.of(5));
    Student student4 = new Student(4, "John", List.of(2));

    service.addStudent(student1);
    service.addStudent(student2);
    service.addStudent(student3);
    service.addStudent(student4);

    Map<Integer, Student> map = service.getStudentsMapById();

    assertEquals(student1, map.get(1));
    assertEquals(student2, map.get(2));
    assertEquals(student3, map.get(3));
    assertEquals(student4, map.get(4));
    assertEquals(4, map.size());
  }

  @Test
  void getStudentsMapById_shouldReturnUnmodifierMap() {
    Map<Integer, Student> map = service.getStudentsMapById();

    assertThrows(
        UnsupportedOperationException.class,
        () -> map.put(100, new Student(100, "Petr")));
  }
}
