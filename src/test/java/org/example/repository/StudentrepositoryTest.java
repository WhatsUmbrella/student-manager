package org.example.repository;

import org.example.model.Student;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {

    @Test
    void save_shouldSaveStudent() {
        StudentRepository repository = new StudentRepository();
        Student student = new Student(1, "Alex");

        repository.save(student);

        Optional<Student> result = repository.findById(1);
        assertTrue(result.isPresent());
        assertEquals(student, result.get());
    }

    @Test
    void findById_shouldReturnEmptyOptionalWhenStudentDoesNotExist() {
        StudentRepository repository = new StudentRepository();

        Optional<Student> result = repository.findById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllStudents() {
        StudentRepository repository = new StudentRepository();
        Student alex = new Student(1, "Alex");
        Student bob = new Student(2, "Bob");

        repository.save(alex);
        repository.save(bob);

        List<Student> students = repository.findAll();

        assertEquals(2, students.size());
        assertTrue(students.contains(alex));
        assertTrue(students.contains(bob));
    }

    @Test
    void existsById_shouldReturnTrueWhenStudentExists() {
        StudentRepository repository = new StudentRepository();
        repository.save(new Student(1, "Alex"));

        boolean exists = repository.existsById(1);

        assertTrue(exists);
    }

    @Test
    void existsById_shouldReturnFalseWhenStudentDoesNotExist() {
        StudentRepository repository = new StudentRepository();

        boolean exists = repository.existsById(1);

        assertFalse(exists);
    }

    @Test
    void deleteById_shouldDeleteStudent() {
        StudentRepository repository = new StudentRepository();
        repository.save(new Student(1, "Alex"));

        repository.deleteById(1);

        assertTrue(repository.findById(1).isEmpty());
    }

    @Test
    void save_shouldReplaceStudentWithSameId() {
        StudentRepository repository = new StudentRepository();
        Student oldStudent = new Student(1, "Alex");
        Student newStudent = new Student(1, "Alexander");

        repository.save(oldStudent);

        repository.save(newStudent);

        List<Student> students = repository.findAll();
        assertEquals(1, students.size());

        Student result = repository.findById(1).orElseThrow();
        assertSame(newStudent, result);
        assertEquals("Alexander", result.getName());
    }

    @Test
    void findAll_shouldReturnUnmodifiableList() {
        StudentRepository repository = new StudentRepository();
        repository.save(new Student(1, "Alex"));

        List<Student> students = repository.findAll();

        assertThrows(
                UnsupportedOperationException.class,
                () -> students.add(new Student(2, "Bob")));
    }

    @Test
    void count_shouldReturnCountOfStudents() {
        StudentRepository repository = new StudentRepository();
        repository.save(new Student(1, "Alex"));
        repository.save(new Student(2, "Bob"));

        int countOfStudents = repository.count();

        assertEquals(2, countOfStudents);
    }

    @Test
    void save_shouldThrowExceptionWhenStudentIsNull() {
        StudentRepository repository = new StudentRepository();
        assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(null));
    }
}
