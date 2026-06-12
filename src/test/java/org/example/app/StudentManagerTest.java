package org.example.app;

import org.example.exception.StudentStorageException;
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

    private void assertStudent(Student student, String name, List<Integer> grades) {
        assertEquals(name, student.getName());
        assertEquals(grades, student.getGrades());
    }

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
        assertStudent(alex, "Alex", List.of(3, 4, 5));
        Student bob = service.findStudentById(2);
        assertStudent(bob, "Bob", List.of(4, 4, 3));
        Student carl = service.findStudentById(3);
        assertStudent(carl, "Carl", List.of(5, 4, 5));
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
        assertStudent(updatedStudent1, "Anna", List.of(5, 5, 5));

        Student updatedStudent3 = service.findStudentById(3);
        assertStudent(updatedStudent3, "Cent", List.of(4, 4));

        Student unchangedStudent2 = service.findStudentById(2);
        assertStudent(unchangedStudent2, "Bob", List.of(4, 4, 3));
    }

    @Test
    void loadFromFile_shouldThrowStorageException_whenFileIsInvalid() throws IOException {
        List<String> lines = List.of("1;Anna");

        Files.write(file, lines);

        assertThrows(
                StudentStorageException.class,
                () -> manager.loadFromFile(file));
    }

    @Test
    void constructor_shouldThrowExceptionWhenServiceIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StudentManager(null, storage));
    }

    @Test
    void constructor_shouldThrowExceptionWhenStorageIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StudentManager(service, null));
    }
}
