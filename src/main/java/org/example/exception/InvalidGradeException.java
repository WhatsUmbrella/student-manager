package org.example.exception;

public class InvalidGradeException extends RuntimeException {
  public InvalidGradeException(int grade) {
    super("Invalid grad: " + grade + ". Grade must be from 1 to 5.");
  }
}
