package org.example.cli;

import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.example.app.StudentManager;
import org.example.storage.FileStudentStorage;

public class Main {
    public static void main(String[] args) {
        StudentRepository repository = new StudentRepository();
        StudentService service = new StudentService(repository);
        FileStudentStorage storage = new FileStudentStorage();
        StudentManager manager = new StudentManager(service, storage);
        StudentConsoleApp app = new StudentConsoleApp(service, manager);
        app.run();
    }
}
