package org.example.storage;

import org.example.model.Student;
import org.example.exception.InvalidGradeException;
import org.example.exception.StudentStorageException;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class FileStudentStorage {
    public void saveStudents(List<Student> students, Path path) {
        if (students == null) {
            throw new StudentStorageException("Students list can't be null.");
        }

        if (path == null) {
            throw new StudentStorageException("Path can't be null.");
        }

        try {
            List<String> lines = studentsToLines(students);
            Files.write(path, lines);
        } catch (StudentStorageException e) {
            throw e;
        } catch (IOException | RuntimeException e) {
            throw new StudentStorageException("Could not save students to file " + path, e);
        }
    }

    public List<Student> loadStudents(Path path) {
        if (path == null) {
            throw new StudentStorageException("Path can't be null.");
        }

        List<Student> students = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                students.add(lineToStudent(line));
            }
        } catch (StudentStorageException e) {
            throw e;
        } catch (IOException | RuntimeException e) {
            throw new StudentStorageException("Could not load students from file " + path, e);
        }
        return students;
    }

    private List<String> studentsToLines(List<Student> students) {
        return students.stream()
                .map(student -> {
                    if (student == null) {
                        throw new StudentStorageException("Student can't be null.");
                    }
                    String grades = student.getGrades().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","));
                    return student.getId() + ";" + student.getName() + ";" + grades;
                })
                .toList();
    }

    private Student lineToStudent(String line) {
        String[] studentInfo = line.split(";", -1);

        if (studentInfo.length != 3) {
            throw new StudentStorageException("Invalid student line format: " + line);
        }

        int id;
        try {
            id = Integer.parseInt(studentInfo[0]);
        } catch (NumberFormatException e) {
            throw new StudentStorageException("Invalid student id: " + studentInfo[0], e);
        }

        Student student;

        try {
            student = new Student(id, studentInfo[1]);
        } catch (IllegalArgumentException e) {
            throw new StudentStorageException("Invalid student data: " + line, e);
        }

        if (!studentInfo[2].isBlank()) {
            String[] grades = studentInfo[2].split(",");
            int i = 0;
            try {
                for (; i < grades.length; i++) {
                    student.addGrade(Integer.parseInt(grades[i]));
                }
            } catch (NumberFormatException | InvalidGradeException e) {
                throw new StudentStorageException("Invalid grade value: " + grades[i], e);
            }
        }

        return student;
    }
}
