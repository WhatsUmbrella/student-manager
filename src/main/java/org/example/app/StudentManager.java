package org.example.app;

import org.example.service.StudentService;
import org.example.storage.FileStudentStorage;
import org.example.model.Student;

import java.nio.file.Path;
import java.util.List;

public class StudentManager {
    private final StudentService service;
    private final FileStudentStorage storage;

    public StudentManager(StudentService service, FileStudentStorage storage) {
        this.service = service;
        this.storage = storage;
    }

    public void saveToFile(Path path) {
        List<Student> students = service.getAllStudents();
        storage.saveStudents(students, path);
    }

    public void loadFromFile(Path path) {
        List<Student> students = storage.loadStudents(path);
        for (Student student : students) {
            service.addStudent(student);
        }
    }
}
