package org.example.repository;

import org.example.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StudentRepository {
  private final Map<Integer, Student> students = new HashMap<>();

  public void save(Student student) {
    students.put(student.getId(), student);
  }

  public Optional<Student> findById(int id) {
    return Optional.ofNullable(students.get(id));
  }

  public List<Student> findAll() {
    return List.copyOf(students.values());
  }

  public boolean existsById(int id) {
    return students.containsKey(id);
  }

  public void deleteById(int id) {
    students.remove(id);
  }

  public int count() {
    return students.size();
  }
}
