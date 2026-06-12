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
    void constructor_shouldThrowExceptionWhenRepositoryIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new StudentService(null));
    }

    @Test
    void addStudent_shouldFindStudentAfterAdded() {
        Student student = new Student(1, "Alex");

        service.addStudent(student);

        Student result = service.findStudentById(1);

        assertSame(student, result);
    }

    @Test
    void findStudentById_shouldReturnStudentWhenStudentExists() {
        Student student = new Student(1, "Alex");

        service.addStudent(student);

        Student result = service.findStudentById(1);

        assertSame(student, result);
    }

    @Test
    void findStudentById_shouldThrowExceptionWhenStudentDoesNotExist() {
        assertThrows(
                StudentNotFoundException.class,
                () -> service.findStudentById(200));
    }

    @Test
    void deleteStudentById_shouldRemoveStudentWhenStudentExists() {
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
    void addGradeToStudent_shouldAddGradeWhenStudentExistsAndGradeIsValid() {
        Student student = new Student(1, "Alex");

        service.addStudent(student);
        service.addGradeToStudent(1, 3);

        assertEquals(List.of(3), service.findStudentById(1).getGrades());
    }

    @Test
    void addGradeToStudent_shouldThrowExceptionWhenStudentDoesNotExist() {
        assertThrows(
                StudentNotFoundException.class,
                () -> service.addGradeToStudent(2, 3));
    }

    @Test
    void addGradeToStudent_shouldThrowExceptionWhenGradeIsInvalid() {
        Student student = new Student(1, "Alex");

        service.addStudent(student);

        assertThrows(
                InvalidGradeException.class,
                () -> service.addGradeToStudent(1, 6));
    }

    @Test
    void getAverageGrade_shouldReturnAverageGradeWhenStudentExists() {
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
    void getAllStudents_shouldReturnUnmodifiableList() {
        List<Student> students = service.getAllStudents();

        assertThrows(
                UnsupportedOperationException.class,
                () -> students.add(new Student(10, "Georg")));
    }

    @Test
    void getAllStudents_shouldReturnEmptyListWhenNoStudentsWereAdded() {
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
        List<Integer> ids = students.stream()
                .map(Student::getId)
                .toList();

        assertEquals(2, ids.size());
        assertTrue(ids.contains(2));
        assertTrue(ids.contains(3));
        assertFalse(ids.contains(1));
    }

    @Test
    void getStudentsWithAverageAbove_shouldNotIncludeStudentsWithAverageEqualToMinAverage() {
        Student student1 = new Student(1, "Alex", List.of(5, 4, 5));
        Student student2 = new Student(2, "Bob", List.of(4, 5));

        service.addStudent(student1);
        service.addStudent(student2);

        List<Student> students = service.getStudentsWithAverageAbove(4.5);
        List<Integer> ids = students.stream()
                .map(Student::getId)
                .toList();

        assertEquals(1, ids.size());
        assertTrue(ids.contains(1));
        assertFalse(ids.contains(2));
    }

    @Test
    void getStudentsWithAverageAbove_shouldReturnUnmodifiableList() {
        List<Student> students = service.getStudentsWithAverageAbove(4.5);

        assertThrows(
                UnsupportedOperationException.class,
                () -> students.add(new Student(10, "Georg")));
    }

    @Test
    void getStudentsSortedByName_shouldReturnStudentsSortedByNameAsc() {
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
    void getStudentsSortedByName_shouldReturnUnmodifiableList() {
        List<Student> students = service.getStudentsSortedByName();

        assertThrows(
                UnsupportedOperationException.class,
                () -> students.add(new Student(10, "Georg")));
    }

    @Test
    void getStudentsSortedByAverageDesc_shouldReturnStudentsSortedByAverageDesc() {
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
    void getStudentsSortedByAverageDesc_shouldSortByNameAscWhenAverageIsEqual() {
        Student student1 = new Student(1, "Alex", List.of(3));
        Student student2 = new Student(2, "Bob", List.of(3));
        Student student3 = new Student(3, "Carl", List.of(5));

        service.addStudent(student1);
        service.addStudent(student2);
        service.addStudent(student3);

        List<Student> students = service.getStudentsSortedByAverageDesc();

        assertEquals(3, students.size());
        assertEquals(5.0, students.get(0).getAverageGrade());
        assertEquals("Alex", students.get(1).getName());
        assertEquals(3.0, students.get(1).getAverageGrade());
        assertEquals("Bob", students.get(2).getName());
        assertEquals(3.0, students.get(2).getAverageGrade());
    }

    @Test
    void getStudentsSortedByAverageDesc_shouldReturnUnmodifiableList() {
        List<Student> students = service.getStudentsSortedByAverageDesc();

        assertThrows(
                UnsupportedOperationException.class,
                () -> students.add(new Student(10, "Georg")));
    }

    @Test
    void getStudentsMapById_shouldReturnStudentsMappedById() {
        Student student1 = new Student(1, "Alex", List.of(3));
        Student student2 = new Student(2, "Bob", List.of(4));
        Student student3 = new Student(3, "Carl", List.of(5));
        Student student4 = new Student(4, "John", List.of(2));

        service.addStudent(student1);
        service.addStudent(student2);
        service.addStudent(student3);
        service.addStudent(student4);

        Map<Integer, Student> map = service.getStudentsMapById();

        assertSame(student1, map.get(1));
        assertSame(student2, map.get(2));
        assertSame(student3, map.get(3));
        assertSame(student4, map.get(4));
        assertEquals(4, map.size());
    }

    @Test
    void getStudentsMapById_shouldReturnUnmodifiableMap() {
        Map<Integer, Student> map = service.getStudentsMapById();

        assertThrows(
                UnsupportedOperationException.class,
                () -> map.put(100, new Student(100, "Petr")));
    }
}
