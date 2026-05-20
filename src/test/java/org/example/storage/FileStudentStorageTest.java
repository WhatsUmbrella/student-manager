package org.example.storage;

import org.example.exception.StudentStorageException;
import org.example.model.Student;

import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class FileStudentStorageTest {
  private FileStudentStorage storage;
  private Path file;

  @TempDir
  Path tempDir;

  @BeforeEach
  void setUp() {
    storage = new FileStudentStorage();
    file = tempDir.resolve("students.txt");
  }

  @Test
  void saveStudents_shouldWritetudentsToFile() throws IOException {
    Student student1 = new Student(1, "Alex", List.of(5, 4, 5));
    Student student2 = new Student(2, "Bob", List.of(3, 4));
    Student student3 = new Student(3, "Anna");
    List<Student> students = List.of(student1, student2, student3);
    storage.saveStudents(students, file);
    List<String> lines = Files.readAllLines(file);
    assertTrue(3 == lines.size());
    assertTrue(lines.contains("1;Alex;5,4,5"));
    assertTrue(lines.contains("2;Bob;3,4"));
    assertTrue(lines.contains("3;Anna;"));
  }

  @Test
  void loadStudents_shouldReadStudentsToFile() throws IOException {
    List<String> lines = List.of(
        "1;Alex;5,4,5",
        "2;Bob;3,4",
        "3;Anna;");
    Files.write(file, lines);

    List<Student> students = storage.loadStudents(file);

    Student student1 = new Student(1, "Alex", List.of(5, 4, 5));
    Student student2 = new Student(2, "Bob", List.of(3, 4));
    Student student3 = new Student(3, "Anna");
    assertTrue(students.size() == 3);
    assertTrue(students.contains(student1));
    assertTrue(students.contains(student2));
    assertTrue(students.contains(student3));
  }

  @Test
  void loadStudents_shouldReturnStudentWithoutGrades_whenGradesPartIsEmpty() throws IOException {
    Files.write(file, "3;Anna;".getBytes());

    List<Student> students = storage.loadStudents(file);

    assertTrue(students.size() == 1);
    assertTrue(students.get(0).getGrades().isEmpty());
    assertEquals(0.0, students.get(0).getAverageGrade());
  }

  @Test
  void saveAndLoadStudents_shouldPreserveData() {
    Student student1 = new Student(1, "Alex", List.of(5, 4, 5));
    Student student2 = new Student(2, "Bob", List.of(3, 4));
    Student student3 = new Student(3, "Anna");

    List<Student> inputStudents = List.of(student1, student2, student3);
    storage.saveStudents(inputStudents, file);
    List<Student> outputStudents = storage.loadStudents(file);
    assertTrue(inputStudents.size() == outputStudents.size());
    assertEquals(inputStudents, outputStudents);
  }

  @Test
  void loadStudents_shouldThrowStorageException_whenFileDoesNotExist() {
    Path nonExistingFile = tempDir.resolve("studnets2.txt");
    assertThrows(
        StudentStorageException.class,
        () -> storage.loadStudents(nonExistingFile).isEmpty());
  }
}
