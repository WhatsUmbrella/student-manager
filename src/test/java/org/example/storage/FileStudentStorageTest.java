package org.example.storage;

import org.example.exception.StudentStorageException;
import org.example.model.Student;

import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileStudentStorageTest {
    private FileStudentStorage storage;
    private Path file;

    private void assertStudent(Student student, int id, String name, List<Integer> grades) {
        assertEquals(id, student.getId());
        assertEquals(name, student.getName());
        assertEquals(grades, student.getGrades());
    }

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        storage = new FileStudentStorage();
        file = tempDir.resolve("students.txt");
    }

    @Test
    void saveStudents_shouldWriteStudentsToFile() throws IOException {
        Student student1 = new Student(1, "Alex", List.of(5, 4, 5));
        Student student2 = new Student(2, "Bob", List.of(3, 4));
        Student student3 = new Student(3, "Anna");

        List<Student> students = List.of(student1, student2, student3);
        storage.saveStudents(students, file);
        List<String> lines = Files.readAllLines(file);

        assertEquals(3, lines.size());
        assertTrue(lines.contains("1;Alex;5,4,5"));
        assertTrue(lines.contains("2;Bob;3,4"));
        assertTrue(lines.contains("3;Anna;"));
    }

    @Test
    void loadStudents_shouldReadStudentsFromFile() throws IOException {
        List<String> lines = List.of(
                "1;Alex;5,4,5",
                "2;Bob;3,4",
                "3;Anna;");

        Files.write(file, lines);

        List<Student> students = storage.loadStudents(file);

        assertEquals(3, students.size());
        assertStudent(students.get(0), 1, "Alex", List.of(5, 4, 5));
        assertStudent(students.get(1), 2, "Bob", List.of(3, 4));
        assertStudent(students.get(2), 3, "Anna", List.of());
    }

    @Test
    void loadStudents_shouldReturnStudentWithoutGrades_whenGradesPartIsEmpty() throws IOException {
        Files.write(file, List.of("3;Anna;"));

        List<Student> students = storage.loadStudents(file);

        assertEquals(1, students.size());
        assertStudent(students.get(0), 3, "Anna", List.of());
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

        assertEquals(inputStudents.size(), outputStudents.size());
        assertStudent(outputStudents.get(0), 1, "Alex", List.of(5, 4, 5));
        assertStudent(outputStudents.get(1), 2, "Bob", List.of(3, 4));
        assertStudent(outputStudents.get(2), 3, "Anna", List.of());
    }

    @Test
    void loadStudents_shouldThrowStorageException_whenFileDoesNotExist() {
        Path nonExistingFile = tempDir.resolve("students2.txt");
        assertThrows(
                StudentStorageException.class,
                () -> storage.loadStudents(nonExistingFile));
    }

    @Test
    void loadStudents_shouldThrowStorageException_whenLineHasInvalidFormat() throws IOException {
        Files.write(file, List.of("1;Alex"));

        assertThrows(
                StudentStorageException.class,
                () -> storage.loadStudents(file));
    }

    @Test
    void loadStudents_shouldThrowStorageException_whenGradeIsNotNumber() throws IOException {
        Files.write(file, List.of("1;Alex;4,wrong,3"));

        assertThrows(
                StudentStorageException.class,
                () -> storage.loadStudents(file));
    }

    @Test
    void loadStudents_shouldThrowStorageException_whenGradeIsOutOfRange() throws IOException {
        Files.write(file, List.of("1;Alex;4,6,3"));

        assertThrows(
                StudentStorageException.class,
                () -> storage.loadStudents(file));
    }

    @Test
    void loadStudents_shouldThrowStorageException_whenIdIsNotNumber() throws IOException {
        Files.write(file, List.of("abc;Alex;4,5"));

        assertThrows(
                StudentStorageException.class,
                () -> storage.loadStudents(file));
    }

    @Test
    void loadStudents_shouldThrowStorageException_whenIdIsNegative() throws IOException {
        Files.write(file, List.of("-1;Alex;4,5"));

        assertThrows(
                StudentStorageException.class,
                () -> storage.loadStudents(file));
    }

    @Test
    void loadStudents_shouldThrowStorageException_whenNameIsEmpty() throws IOException {
        Files.write(file, List.of("1;;4,5"));

        assertThrows(
                StudentStorageException.class,
                () -> storage.loadStudents(file));
    }

    @Test
    void saveStudents_shouldThrowStorageException_whenStudentsListIsNull() {
        assertThrows(
                StudentStorageException.class,
                () -> storage.saveStudents(null, file));
    }

    @Test
    void saveStudents_shouldThrowStorageException_whenPathIsNull() {
        List<Student> students = List.of(
                new Student(1, "Alex", List.of(3, 4, 5)),
                new Student(2, "Bob", List.of(3, 3)));
        assertThrows(
                StudentStorageException.class,
                () -> storage.saveStudents(students, null));
    }

    @Test
    void loadStudents_shouldThrowStorageException_whenPathIsNull() {
        assertThrows(
                StudentStorageException.class,
                () -> storage.loadStudents(null));
    }

    @Test
    void saveStudents_shouldThrowStorageException_whenStudentsListContainsNull() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "Alex"));
        students.add(null);

        assertThrows(
                StudentStorageException.class,
                () -> storage.saveStudents(students, file));
    }
}
