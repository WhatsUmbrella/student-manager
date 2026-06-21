package org.example.cli;

import org.example.service.StudentService;
import org.example.model.Student;
import org.example.exception.InvalidGradeException;
import org.example.exception.StudentNotFoundException;


import java.util.Scanner;
import java.util.List;
import java.util.stream.Collectors;

public class StudentConsoleApp {
    private static final String MENU = """
            === Student Manager ===

            1. Show all students
            2. Add student
            3. Add grade to student
            4. Find student by id
            0. Exit

            """;

    private final StudentService service;
    private final Scanner scanner;

    public StudentConsoleApp(StudentService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service can't be null.");
        }
        this.service = service;
        this.scanner = new Scanner(System.in);
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
            System.out.println(formatStudent(student));
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
            System.out.println(formatStudent(student));
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

    private String formatStudent(Student student) {
        String grades;
        List<Integer> gradesList = student.getGrades();

        if (gradesList.isEmpty()) {
            grades = "";
        } else {
            grades = gradesList.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
        }

        return String.format("ID: %d | Name: %s | Average: %.1f | Grades: [%s]",
                student.getId(), student.getName(), student.getAverageGrade(), grades);
    }
}
