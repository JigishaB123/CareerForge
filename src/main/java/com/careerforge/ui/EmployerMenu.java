package com.careerforge.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

import com.careerforge.job.*;
import com.careerforge.application.BaseApplication;
import com.careerforge.portal.CareerPortal;
import com.careerforge.user.Employer;
import com.careerforge.user.Student;

public class EmployerMenu {

    private Scanner scanner;
    private Employer currentEmployer;

    public EmployerMenu(Employer employer) {
        this.scanner = new Scanner(System.in);
        this.currentEmployer = employer;
    }

    private void syncPostedJobsFromPortal() {
        List<JobPosting> allJobs = CareerPortal.getInstance().getJobPostings();
        String company = currentEmployer.getCompanyName();
        List<String> trackedIds = currentEmployer.getPostedJobs().stream()
            .map(JobPosting::getJobId)
            .collect(Collectors.toList());
        for (JobPosting job : allJobs) {
            if (job.getCompany().equalsIgnoreCase(company) && !trackedIds.contains(job.getJobId())) {
                currentEmployer.postJob(job);
            }
        }
    }

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== EMPLOYER DASHBOARD ===");
            System.out.println("Company: " + currentEmployer.getCompanyName());
            System.out.println("1. Post a New Job");
            System.out.println("2. View Posted Jobs");
            System.out.println("3. View Applications");
            System.out.println("4. Manage Candidate Status");
            System.out.println("5. View Notifications");
            System.out.println("6. Logout");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    postNewJob();
                    break;
                case "2":
                    viewPostedJobs();
                    break;
                case "3":
                    viewApplications();
                    break;
                case "4":
                    manageCandidateStatus();
                    break;
                case "5":
                    viewNotifications();
                    break;
                case "6":
                    System.out.println("Logging out...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void postNewJob() {
        System.out.println("\n--- Post a New Job ---");
        System.out.print("Enter Job Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter Location (e.g., Remote, Boston): ");
        String location = scanner.nextLine().trim();

        System.out.print("Enter Required Skills (comma separated): ");
        String skills = scanner.nextLine().trim();

        System.out.print("Enter Preferred Major: ");
        String major = scanner.nextLine().trim();

        System.out.print("Enter Application Deadline (YYYY-MM-DD): ");
        String deadline = scanner.nextLine().trim();

        System.out.println("Select Job Type:");
        System.out.println("1. Full-Time");
        System.out.println("2. Internship");
        System.out.println("3. Co-Op");
        System.out.print("Choice: ");
        String typeChoice = scanner.nextLine().trim();

        Map<String, String> details = new HashMap<>();
        details.put(
            "jobId",
            "JOB-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase()
        );
        details.put("title", title);
        details.put("company", currentEmployer.getCompanyName());
        details.put("location", location);
        details.put("requiredSkills", skills);
        details.put("preferredMajor", major);
        details.put("status", "OPEN");
        details.put("deadline", deadline);

        JobPostingFactory factory;
        if (typeChoice.equals("1")) {
            factory = new FullTimeFactory();
            System.out.print("Enter Minimum Salary: ");
            details.put("salaryMin", scanner.nextLine().trim());
            System.out.print("Enter Maximum Salary: ");
            details.put("salaryMax", scanner.nextLine().trim());
            System.out.print("Includes Benefits? (true/false): ");
            details.put("hasBenefits", scanner.nextLine().trim());
            System.out.print("Enter Signing Bonus amount: ");
            details.put("signingBonus", scanner.nextLine().trim());
        } else if (typeChoice.equals("2")) {
            factory = new InternshipFactory();
            System.out.print("Enter Duration in Months (whole number): ");
            details.put("duration", scanner.nextLine().trim());
            System.out.print("Enter Total Stipend: ");
            details.put("stipend", scanner.nextLine().trim());
            System.out.print("Provides Academic Credit? (true/false): ");
            details.put("academicCredit", scanner.nextLine().trim());
        } else if (typeChoice.equals("3")) {
            factory = new CoOpFactory();
            System.out.print("Enter Rotation Cycle (e.g., Spring/Fall): ");
            details.put("rotationCycle", scanner.nextLine().trim());
            System.out.print("Enter Duration in Months: ");
            details.put("durationMonths", scanner.nextLine().trim());
        } else {
            System.out.println(
                "Error: Invalid job type selected. Job posting cancelled."
            );
            return;
        }

        try {
            JobPosting newJob = factory.createPosting(details);
            currentEmployer.postJob(newJob);
            CareerPortal.getInstance().addJobPosting(newJob);
            System.out.println("Success! Job '" + title + "' has been posted.");
        } catch (Exception e) {
            System.out.println("Error posting job: " + e.getMessage());
        }
    }

    private void viewPostedJobs() {
        System.out.println("\n--- Your Posted Jobs ---");
        if (
            currentEmployer.getPostedJobs() == null ||
            currentEmployer.getPostedJobs().isEmpty()
        ) {
            System.out.println("You haven't posted any jobs yet.");
            return;
        }

        for (JobPosting job : currentEmployer.getPostedJobs()) {
            job.display();
            System.out.println("-------------------------");
        }
    }

    private void viewApplications() {
        System.out.println("\n=== Applications for Your Jobs ===");
        CareerPortal portal = CareerPortal.getInstance();
        String company = currentEmployer.getCompanyName();
        List<JobPosting> myJobs = portal.getJobPostings().stream()
            .filter(j -> j.getCompany().equalsIgnoreCase(company))
            .collect(Collectors.toList());

        if (myJobs.isEmpty()) {
            System.out.println("You haven't posted any jobs yet.");
            return;
        }

        boolean anyApplications = false;
        for (JobPosting job : myJobs) {
            List<BaseApplication> apps = portal.getApplicationsForJob(job.getJobId());
            if (apps.isEmpty()) continue;

            anyApplications = true;
            System.out.println("\n--------------------------------------------------");
            System.out.println("Job : " + job.getTitle() + " (" + job.getJobId() + ") | " + job.getType());
            System.out.println("--------------------------------------------------");

            for (int i = 0; i < apps.size(); i++) {
                BaseApplication app = apps.get(i);
                Student student = app.getStudent();
                System.out.println("\n  Application #" + (i + 1) + " — " + app.getApplicationId());
                System.out.println("  Student  : " + student.getFirstName() + " " + student.getLastName()
                    + " (ID: " + student.getUserId() + ")");
                System.out.println("  Email    : " + student.getEmail());
                System.out.println("  Major    : " + student.getMajor()
                    + " | GPA: " + student.getGpa()
                    + " | Grad Year: " + student.getGraduationYear());
                System.out.println("  State    : " + app.getCurrentState().getStateName());
                System.out.println("  History  : " + String.join("\n           ", app.getStatusHistory()));
            }
        }

        if (!anyApplications) {
            System.out.println("No applications received for your jobs yet.");
        }
    }

    private void manageCandidateStatus() {
        System.out.println("\n=== Manage Candidate Status ===");
        CareerPortal portal = CareerPortal.getInstance();
        String company = currentEmployer.getCompanyName();

        List<BaseApplication> allApps = portal.getJobPostings().stream()
            .filter(j -> j.getCompany().equalsIgnoreCase(company))
            .flatMap(j -> portal.getApplicationsForJob(j.getJobId()).stream())
            .collect(Collectors.toList());

        if (allApps.isEmpty()) {
            System.out.println("No applications received for your jobs yet.");
            return;
        }

        System.out.println();
        for (int i = 0; i < allApps.size(); i++) {
            BaseApplication app = allApps.get(i);
            Student student = app.getStudent();
            System.out.println((i + 1) + ". [" + app.getCurrentState().getStateName() + "]"
                + " " + student.getFirstName() + " " + student.getLastName()
                + " -> " + app.getJob().getTitle()
                + " (" + app.getApplicationId() + ")");
        }

        System.out.print("\nSelect application to update (0 to cancel): ");
        int idx;
        try {
            idx = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }
        if (idx <= 0 || idx > allApps.size()) {
            System.out.println("Cancelled.");
            return;
        }

        BaseApplication app = allApps.get(idx - 1);
        String currentState = app.getCurrentState().getStateName();
        System.out.println("\nApplication : " + app.getApplicationId());
        System.out.println("Student     : " + app.getStudent().getFirstName() + " " + app.getStudent().getLastName());
        System.out.println("Job         : " + app.getJob().getTitle());
        System.out.println("Current State: " + currentState);
        System.out.println("History     : " + String.join("\n           ", app.getStatusHistory()));

        System.out.println("\nAvailable actions:");
        switch (currentState) {
            case "Submitted":
                System.out.println("1. Move to Under Review");
                System.out.println("2. Reject");
                System.out.print("Choice: ");
                switch (scanner.nextLine().trim()) {
                    case "1": app.review();  break;
                    case "2": app.reject();  break;
                    default:  System.out.println("Invalid choice."); return;
                }
                break;
            case "UnderReview":
                System.out.println("1. Schedule Interview");
                System.out.println("2. Reject");
                System.out.print("Choice: ");
                switch (scanner.nextLine().trim()) {
                    case "1": app.scheduleInterview(); break;
                    case "2": app.reject();            break;
                    default:  System.out.println("Invalid choice."); return;
                }
                break;
            case "Interview":
                System.out.println("1. Make Offer");
                System.out.println("2. Reject");
                System.out.print("Choice: ");
                switch (scanner.nextLine().trim()) {
                    case "1": app.makeOffer(); break;
                    case "2": app.reject();    break;
                    default:  System.out.println("Invalid choice."); return;
                }
                break;
            case "Offer":
                System.out.println("1. Accept (confirm hire)");
                System.out.println("2. Reject");
                System.out.print("Choice: ");
                switch (scanner.nextLine().trim()) {
                    case "1": app.accept(); break;
                    case "2": app.reject(); break;
                    default:  System.out.println("Invalid choice."); return;
                }
                break;
            case "Accepted":
            case "Rejected":
            case "Withdrawn":
                System.out.println("This application is already in a final state (" + currentState + "). No further actions available.");
                return;
            default:
                System.out.println("No actions available for state: " + currentState);
                return;
        }

        CareerPortal.getInstance().notifyEvent("APPLICATION_STATUS_CHANGED", app);
        System.out.println("\nStatus updated to: " + app.getCurrentState().getStateName());
        System.out.println("History: " + String.join("\n           ", app.getStatusHistory()));
    }

    private void viewNotifications() {
        System.out.println("\n=== Notifications ===");
        List<String> notifications = currentEmployer.getNotifications();
        if (notifications.isEmpty()) {
            System.out.println("No new notifications.");
            return;
        }
        for (int i = 0; i < notifications.size(); i++) {
            System.out.println((i + 1) + ". " + notifications.get(i));
        }
        System.out.print("\nClear all notifications? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            notifications.clear();
            System.out.println("Notifications cleared.");
        }
    }
}
