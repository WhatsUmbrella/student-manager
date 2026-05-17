package org.example.service;

import org.example.repository.StudentRepository;
import org.example.model.Student;
import org.example.exception.*;

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
}
