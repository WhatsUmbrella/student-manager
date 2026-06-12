package org.example.service;

import org.example.repository.StudentRepository;
import org.example.model.Student;
import org.example.exception.StudentNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.function.Function;

public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository can't be null.");
        }
        this.repository = repository;
    }

    public void addStudent(Student student) {
        repository.save(student);
    }

    public Student findStudentById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public void deleteStudentById(int id) {
        if (!repository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }

        repository.deleteById(id);
    }

    public void addGradeToStudent(int id, int grade) {
        Student student = findStudentById(id);
        student.addGrade(grade);
    }

    public double getAverageGrade(int id) {
        return findStudentById(id).getAverageGrade();
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public List<Student> getStudentsWithAverageAbove(double minAverage) {
        return getAllStudents().stream()
                .filter(student -> student.getAverageGrade() > minAverage)
                .toList();
    }

    public List<Student> getStudentsSortedByName() {
        return getAllStudents().stream()
                .sorted(Comparator.comparing(Student::getName))
                .toList();
    }

    public List<Student> getStudentsSortedByAverageDesc() {
        return getAllStudents().stream()
                .sorted(Comparator.comparingDouble(Student::getAverageGrade)
                        .reversed()
                        .thenComparing(Student::getName))
                .toList();
    }

    public Map<Integer, Student> getStudentsMapById() {
        return getAllStudents().stream()
                .collect(Collectors.toUnmodifiableMap(
                        Student::getId,
                        Function.identity()));
    }
}
