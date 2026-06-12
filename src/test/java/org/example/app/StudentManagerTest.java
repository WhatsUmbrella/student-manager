package org.example.app;

import org.example.model.Student;
import org.example.service.StudentService;
import org.example.storage.FileStudentStorage;
import org.example.repository.StudentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

class StudentManagerTest {
    private FileStudentStorage storage;
    private StudentService service;
    private StudentRepository repository;
    private StudentManager manager;
    private Path file;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        file = tempDir.resolve("students.txt");
        storage = new FileStudentStorage();
        repository = new StudentRepository();
        service = new StudentService(repository);
        manager = new StudentManager(service, storage);
    }

    @Test
    void saveToFile_shouldSaveAllStudentsFromService() throws IOException {
        List<Student> students = List.of(
                new Student(1, "Alex", List.of(3, 4, 5)),
                new Student(2, "Bob", List.of(4, 4, 3)),
                new Student(3, "Carl", List.of(5, 4, 5)));

        for (Student student : students) {
            service.addStudent(student);
        }

        manager.saveToFile(file);

        List<String> lines = Files.readAllLines(file);
        assertEquals(3, lines.size());
        assertTrue(lines.contains("1;Alex;3,4,5"));
        assertTrue(lines.contains("2;Bob;4,4,3"));
        assertTrue(lines.contains("3;Carl;5,4,5"));
    }

    @Test
    void loadFromFile_shouldLoadStudentsIntoService() throws IOException {
        List<String> lines = List.of(
                "1;Alex;3,4,5",
                "2;Bob;4,4,3",
                "3;Carl;5,4,5");
        Files.write(file, lines);

        manager.loadFromFile(file);

        List<Student> students = service.getAllStudents();

        assertEquals(3, students.size());
        Student alex = service.findStudentById(1);
        assertEquals("Alex", alex.getName());
        assertEquals(List.of(3, 4, 5), alex.getGrades());
        Student bob = service.findStudentById(2);
        assertEquals("Bob", bob.getName());
        assertEquals(List.of(4, 4, 3), bob.getGrades());
        Student carl = service.findStudentById(3);
        assertEquals("Carl", carl.getName());
        assertEquals(List.of(5, 4, 5), carl.getGrades());
    }

    @Test
    void loadFromFile_shouldReplaceStudentWithSameId() throws IOException {
        List<Student> studentsToService = List.of(
                new Student(1, "Alex", List.of(3, 4, 5)),
                new Student(2, "Bob", List.of(4, 4, 3)),
                new Student(3, "Carl", List.of(5, 4, 5)));

        for (Student student : studentsToService) {
            service.addStudent(student);
        }

        List<String> lines = List.of(
                "1;Anna;5,5,5",
                "3;Cent;4,4");

        Files.write(file, lines);

        manager.loadFromFile(file);

        List<Student> students = service.getAllStudents();

        assertEquals(3, students.size());
        Student updatedStudent1 = service.findStudentById(1);
        assertEquals("Anna", updatedStudent1.getName());
        assertEquals(List.of(5, 5, 5), updatedStudent1.getGrades());

        Student updatedStudent3 = service.findStudentById(3);
        assertEquals("Cent", updatedStudent3.getName());
        assertEquals(List.of(4, 4), updatedStudent3.getGrades());

        Student unchangedStudent2 = service.findStudentById(2);
        assertEquals("Bob", unchangedStudent2.getName());
        assertEquals(List.of(4, 4, 3), unchangedStudent2.getGrades());
    }
}
