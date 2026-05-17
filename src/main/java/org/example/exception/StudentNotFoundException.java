package org.example.exception;

public class StudentNotFoundException extends RuntimeException {
  public StudentNotFoundException(int id) {
    super("Student with id " + id + " not found.");
  }
}
