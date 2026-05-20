package org.example.storage;

//my imports
import org.example.model.Student;
import org.example.exception.InvalidGradeException;
import org.example.exception.StudentStorageException;

//jc imort
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class FileStudentStorage {
  public void saveStudents(List<Student> students, Path path) {
    List<String> lines = studentsToLines(students);
    try {
      Files.write(path, lines);
    } catch (IOException | RuntimeException e) {
      throw new StudentStorageException("Could not save students to file " + path, e);
    }
  }

  public List<Student> loadStudents(Path path) {
    List<Student> students = new ArrayList<>();

    try {
      List<String> lines = Files.readAllLines(path);
      for (String line : lines) {
        students.add(lineToStudent(line));
      }
    } catch (IOException | RuntimeException e) {
      throw new StudentStorageException("Could not load students from file " + path, e);
    }

    return students;
  }

  private List<String> studentsToLines(List<Student> students) {
    return students.stream()
        .map(student -> {
          String grades = student.getGrades().stream().map(String::valueOf).collect(Collectors.joining(","));
          return student.getId() + ";" + student.getName() + ";" + grades;
        }).toList();
  }

  private Student lineToStudent(String line) {
    String[] studentInfo = line.split(";", -1);
    if (studentInfo.length != 3) {
      throw new StudentStorageException("Invalid student line format: " + line);
    }
    Student student = new Student(Integer.parseInt(studentInfo[0]),
        studentInfo[1]);
    if (!studentInfo[2].isBlank()) {
      String[] grades = studentInfo[2].split(",");
      int i = 0;
      try {
        for (; i < grades.length; i++) {
          student.addGrade(Integer.parseInt(grades[i]));
        }
      } catch (NumberFormatException | InvalidGradeException e) {
        throw new StudentStorageException("Invalid grade format: " + grades[i], e);
      }
    }

    return student;
  }
}
