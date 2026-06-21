package org.example.cli;

import org.example.repository.StudentRepository;
import org.example.service.StudentService;

public class Main {
    public static void main(String[] args) {
        StudentRepository repository = new StudentRepository();
        StudentService service = new StudentService(repository);
        StudentConsoleApp app = new StudentConsoleApp(service);
        app.run();
    }
}
