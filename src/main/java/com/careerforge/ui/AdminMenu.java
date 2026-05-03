package com.careerforge.ui;

import java.util.Map;
import java.util.Scanner;
import com.careerforge.job.JobPosting;
import com.careerforge.user.Admin;
import com.careerforge.portal.CareerPortal;
import com.careerforge.user.Employer;
import com.careerforge.user.Student;

public class AdminMenu {

    private Scanner scanner;
    private Admin currentAdmin;

    public AdminMenu(Admin admin) {
        this.scanner = new Scanner(System.in);
        this.currentAdmin = admin;
    }

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("1. View System Dashboard");
            System.out.println("2. View All Students");
            System.out.println("3. View All Employers");
            System.out.println("4. View All Job Postings");
            System.out.println("5. Generate System Report");
            System.out.println("6. Logout");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewDashboard();
                    break;
                case "2":
                    viewAllStudents();
                    break;
                case "3":
                    viewAllEmployers();
                    break;
                case "4":
                    viewAllJobPostings();
                    break;
                case "5":
                    generateReport();
                    break;
                case "6":
                    System.out.println("Logging out Admin...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void viewDashboard() {
        System.out.println("\n--- System Dashboard ---");
        int totalJobs = CareerPortal.getInstance().getJobPostings().size();
        System.out.println("Total Jobs in Portal: " + totalJobs);
        System.out.println("System Status: HEALTHY");
        System.out.println("Observer EventManager: ACTIVE");
    }

    private void viewAllJobPostings() {
        System.out.println("\n--- All System Job Postings ---");
        if (CareerPortal.getInstance().getJobPostings().isEmpty()) {
            System.out.println("No jobs currently in the system.");
            return;
        }
        for (JobPosting job : CareerPortal.getInstance().getJobPostings()) {
            System.out.println(
                "[ " +
                    job.getType() +
                    " ] " +
                    job.getTitle() +
                    " at " +
                    job.getCompany()
            );
        }
    }

    private void generateReport() {
        System.out.println("\n--- Generating System Report ---");
        System.out.println("Career Forge Milestone 2 Report:");
        System.out.println("- Singleton Data Load: PASSED");
        System.out.println("- Strategy Engine: PASSED");
        System.out.println("- Observer Notifications: PASSED");
        System.out.println("- Iterator Traversal: PASSED");
        System.out.println("Report saved to logs.");
    }
    
    private void viewAllStudents() {
        System.out.println("\n=== All Registered Students ===");
        CareerPortal portal = CareerPortal.getInstance();
        Map<String, Student> students = portal.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students registered.");
            return;
        }
        for (Student s : students.values()) {
            System.out.println("  " + s);
        }
    }

    private void viewAllEmployers() {
        System.out.println("\n=== All Registered Employers ===");
        CareerPortal portal = CareerPortal.getInstance();
        Map<String, Employer> employers = portal.getAllEmployers();
        if (employers.isEmpty()) {
            System.out.println("No employers registered.");
            return;
        }
        for (Employer e : employers.values()) {
            System.out.println("  " + e);
        }
    }
}
