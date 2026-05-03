package com.careerforge.ui;

import java.util.Scanner;
import com.careerforge.user.Admin;
import com.careerforge.portal.CareerPortal;
import com.careerforge.user.Employer;
import com.careerforge.user.Student;

/**
 * The entry point UI for the Career Forge application.
 * Handles user registration and routing to specific sub-menus.
 */
public class MainMenu {

    private final Scanner scanner;

    public MainMenu() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the main menu loop.
     */
    public void display() {
        boolean running = true;

        while (running) {
            System.out.println("\n==============================");
            System.out.println("    Welcome to Career Forge   ");
            System.out.println("==============================");
            System.out.println("1. Register Student");
            System.out.println("2. Student Login");
            System.out.println("3. Employer Login");
            System.out.println("4. Admin Login");
            System.out.println("5. Exit");
            System.out.print("Please choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleStudentRegistration();
                    break;
                case "2":
                    handleStudentLogin();
                    break;
                case "3":
                    handleEmployerLogin();
                    break;
                case "4":
                    handleAdminLogin();
                    break;
                case "5":
                    System.out.println(
                        "Thank you for using Career Forge. Goodbye!"
                    );
                    running = false;
                    break;
                default:
                    System.out.println(
                        "Invalid option. Please enter a number between 1 and 5."
                    );
            }
        }
    }

    private void handleStudentRegistration() {
        System.out.println("\n--- Student Registration ---");
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine().trim();
        String email = "";
        while (true) {
            System.out.print("Enter Email: ");
            email = scanner.nextLine().trim();

            if (isEmailTaken(email)) {
                System.out.println(
                    "Error: An account with that email already exists. Please try another one."
                );
            } else {
                break; 
            }
        }
        String password = readPassword("Enter Password: ");
        String id = generateStudentId();
        System.out.println("Assigned Student ID: " + id);

        Student student = new Student(
            id,
            firstName,
            lastName,
            email,
            password,
            "Undeclared",
            0.0,
            0
        );

        CareerPortal.getInstance().registerStudent(student);
        System.out.println("Registration successful! You can now log in.");
    }

    private void handleStudentLogin() {
        System.out.print("\nEnter Student ID: ");
        String id = scanner.nextLine().trim();
        String password = readPassword("Enter Password: ");

        CareerPortal portal = CareerPortal.getInstance();
        Student student = portal.getStudentById(id);
        if (student != null && student.getPassword().equals(password)) {
            System.out.println(
                "Login successful! Welcome, " + student.getFirstName()
            );
            new StudentMenu(student, scanner).display();
        } else System.out.println("Error: Invalid Student ID or Password.");
    }

    private void handleEmployerLogin() {
        System.out.print("\nEnter Employer ID to Login: ");
        String id = scanner.nextLine().trim();
        String password = readPassword("Enter Password: ");

        Employer employer = CareerPortal.getInstance().getEmployerById(id);

        if (employer != null && employer.getPassword().equals(password)) {
            System.out.println(
                "Login successful! Welcome, " +
                    employer.getFirstName() +
                    " from " +
                    employer.getCompanyName()
            );
            new EmployerMenu(employer).display();
        } else {
            System.out.println("Error: Invalid Employer ID or Password.");
        }
    }

    private void handleAdminLogin() {

        Admin admin = new Admin(
            "id",
            "System",
            "Administrator",
            "admin@careerforge.com",
            "password",
            "SuperAdmin"
        );
        System.out.println("Login successful! Welcome, Admin.");
        new AdminMenu(admin).display();
    }

    private String generateStudentId() {
        int maxId = 0;

        for (String existingId : CareerPortal.getInstance()
            .getAllStudents()
            .keySet()) {
            if (existingId.startsWith("STU")) {
                try {
                    int num = Integer.parseInt(existingId.substring(3));
                    if (num > maxId) {
                        maxId = num;
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Bad ID error ");
                }
            }
        }

        return String.format("STU%03d", maxId + 1);
    }

    private String readPassword(String prompt) {
        java.io.Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword(prompt);
            return new String(passwordArray).trim();
        } else {
            System.out.print(prompt);
            return scanner.nextLine().trim();
        }
    }

    private boolean isEmailTaken(String email) {
        for (Student existingStudent : CareerPortal.getInstance()
            .getAllStudents()
            .values()) {
            if (existingStudent.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}
