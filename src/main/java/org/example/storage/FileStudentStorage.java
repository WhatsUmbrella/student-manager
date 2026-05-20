package org.example.storage;

//my imports
import org.example.model.Student;
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
    if (students.isEmpty()) {
      System.out.println("You submitted an empty list of students");
      return;
    }
    List<String> lines = studentsToLines(students);
    try {
      Files.write(path, lines);
    } catch (IOException e) {
      throw new StudentStorageException(e.getMessage(), e);
    }
  }

  public List<Student> loadStudents(Path path) {
    List<Student> students = new ArrayList<>();

    try {
      List<String> lines = Files.readAllLines(path);
      for (String line : lines) {
        students.add(lineToStudnet(line));
      }
    } catch (IOException e) {
      throw new StudentStorageException(e.getMessage(), e);
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

  private Student lineToStudnet(String line) {
    String[] studentInfo = line.split(";");
    Student student = new Student(Integer.parseInt(studentInfo[0]),
        studentInfo[1]);
    if (studentInfo.length > 2) {
      String[] grades = studentInfo[2].split(",");
      for (String grade : grades) {
        student.addGrade(Integer.parseInt(grade));
      }
    }

    return student;
  }
}
