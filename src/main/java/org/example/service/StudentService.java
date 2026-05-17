package org.example.service;

import org.example.repository.StudentRepository;
import org.example.model.Student;
import org.example.exception.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;

public class StudentService {
  private final StudentRepository repository;

  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public void addStudent(Student student) {
    repository.save(student);
  }

  public Student findStudentById(int id) {
    return repository.findById(id)
        .orElseThrow(
            () -> new StudentNotFoundException(id));
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

  public List<Student> getAllStudent() {
    return repository.findAll();
  }

  public List<Student> getStudentsWithAverageAbove(double minAverage) {
    return getAllStudent().stream()
        .filter(student -> student.getAverageGrade() > minAverage)
        .toList();
  }

  public List<Student> getStudentsSortedByName() {
    return getAllStudent().stream()
        .sorted(Comparator.comparing(Student::getName))
        .toList();
  }

  public List<Student> getStudentsSortedByAverageDesc() {
    return getAllStudent().stream()
        .sorted(Comparator.comparingDouble(Student::getAverageGrade)
            .reversed()
            .thenComparing(Student::getName))
        .toList();
  }

  public Map<Integer, Student> getStudentsMapById() {
    return getAllStudent().stream()
        .collect(Collectors.collectingAndThen(
            Collectors.toMap(Student::getId, student -> student),
            Collections::unmodifiableMap));
  }
}
