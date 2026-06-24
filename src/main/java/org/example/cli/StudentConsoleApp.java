package org.example.cli;

import org.example.service.StudentService;
import org.example.model.Student;
import org.example.exception.InvalidGradeException;
import org.example.exception.StudentNotFoundException;

import java.util.Scanner;
import java.util.List;

public class StudentConsoleApp {
    private static final String MENU = """
            === Student Manager ===

            1. Show all students
            2. Add student
            3. Add grade to student
            4. Find student by id
            5. Delete student
            0. Exit

            """;

    private final StudentService service;
    private final Scanner scanner;
    private final StudentFormatter formatter;

    public StudentConsoleApp(StudentService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service can't be null.");
        }
        this.service = service;
        this.scanner = new Scanner(System.in);
        this.formatter = new StudentFormatter();
    }

    public void run() {
        while (true) {
            printMenu();

            int option = readInt("Choose option: ");

            if (!handleOption(option)) {
                break;
            }
        }
    }

    private void printMenu() {
        System.out.print(MENU);
    }

    private boolean handleOption(int option) {
        switch (option) {
            case 1 -> {
                showAllStudents();
                return true;
            }
            case 2 -> {
                addStudent();
                return true;
            }
            case 3 -> {
                addGradeToStudent();
                return true;
            }
            case 4 -> {
                findStudentById();
                return true;
            }
            case 5 -> {
                deleteStudentById();
                return true;
            }
            case 0 -> {
                System.out.println("Goodbye!");
                return false;
            }
            default -> {
                System.out.println("Unknown option.");
                return true;
            }
        }
    }

    private void showAllStudents() {
        List<Student> students = service.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        for (Student student : students) {
            System.out.println(formatter.format(student));
        }
    }

    private void addStudent() {
        int id = readInt("Enter student id: ");

        System.out.print("Enter student name: ");

        String name = scanner.nextLine();

        try {
            service.addStudent(new Student(id, name));
            System.out.println("Student added.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addGradeToStudent() {
        int id = readInt("Enter student id: ");
        int grade = readInt("Enter grade: ");

        try {
            service.addGradeToStudent(id, grade);
            System.out.println("Grade added.");
        } catch (StudentNotFoundException | InvalidGradeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findStudentById() {
        int id = readInt("Enter student id: ");

        try {
            Student student = service.findStudentById(id);
            System.out.println(formatter.format(student));
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteStudentById() {
        int id = readInt("Enter student id: ");

        try {
            service.deleteStudentById(id);
            System.out.println("Student deleted.");
        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private int readInt(String prompt) {
        int number;

        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();

            try {
                number = Integer.parseInt(line);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }

        return number;
    }
}
