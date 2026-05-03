package com.careerforge.ui;

import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import com.careerforge.resume.Education;
import com.careerforge.resume.Experience;
import com.careerforge.resume.Project;
import com.careerforge.resume.ResumeBuilder;
import com.careerforge.application.ApplicationComponent;
import com.careerforge.application.ApplicationDecorator;
import com.careerforge.application.BaseApplication;
import com.careerforge.application.CoverLetterDecorator;
import com.careerforge.application.PortfolioDecorator;
import com.careerforge.application.RecommendationDecorator;
import com.careerforge.application.ReferralDecorator;
import com.careerforge.job.JobPosting;
import com.careerforge.job.JobCollection;
import com.careerforge.job.JobIterator;
import com.careerforge.job.JobListingCollection;
import com.careerforge.portal.CareerPortal;
import com.careerforge.user.Student;
import com.careerforge.recommendation.LocationMatchStrategy;
import com.careerforge.recommendation.MajorMatchStrategy;
import com.careerforge.recommendation.RecommendationEngine;
import com.careerforge.recommendation.RecommendationStrategy;
import com.careerforge.recommendation.SkillMatchStrategy;
public class StudentMenu {

    private CareerPortal portal;
    private Student student;
    private Scanner scanner;
    private JobListingCollection jobCollection;

    StudentMenu(Student student, Scanner scanner) {
        this.portal = CareerPortal.getInstance();
        this.student = student;
        this.scanner = scanner;
        this.jobCollection = new JobListingCollection();
        portal.getJobPostings().forEach(jobCollection::addJob);
    }

    public void display() {
        while (true) {
            System.out.println("\n======== Student Menu =========");
            System.out.println("1. Browse Jobs");
            System.out.println("2. Apply to a Job");
            System.out.println("3. View My Applications");
            System.out.println("4. Get Job Recommendations");
            System.out.println("5. Enhance Application");
            System.out.println("6. Find a Mentor");
            System.out.println("7. View Notifications");
            System.out.println("8. View My Profile");
            System.out.println("0. Logout");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> browseJobs();
                case "2" -> applyForJob();
                case "3" -> viewApplications();
                case "4" -> getRecommendations();
                case "5" -> enhanceApplication();
                case "6" -> findMentor();
                case "7" -> viewNotifications();
                case "8" -> viewProfile();
                case "0" -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    
    private void browseJobs() {
        System.out.println("\n--- Browse Jobs ---");
        System.out.println("1. View All  (paginated)");
        System.out.println("2. Filter by Type / Location / Skills");
        System.out.print("Choose: ");

        switch (scanner.nextLine().trim()) {
            case "1" -> paginatedDisplay(jobCollection.createIterator());
            case "2" -> filteredDisplay(jobCollection);
            default -> System.out.println("Invalid option.");
        }
    }

    private void paginatedDisplay(JobIterator it) {
        int shown = 0;
        while (it.hasNext()) {
            printJob(++shown, it.next());
            if (shown % 5 == 0 && it.hasNext()) {
                System.out.print("Next page? (y/n): ");
                if (!isYes()) break;
            }
        }
        if (shown == 0) System.out.println("No jobs available.");
    }

    private void filteredDisplay(JobCollection collection) {
        System.out.println("Filter by:");
        System.out.println("1. Type     (Internship / Co-Op / Full-Time)");
        System.out.println("2. Location");
        System.out.println("3. Skill");
        System.out.print("Choose: ");

        String filterType = switch (scanner.nextLine().trim()) {
            case "1" -> "type";
            case "2" -> "location";
            case "3" -> "skill";
            default -> "type";
        };

        System.out.print("Enter value: ");
        String filterValue = scanner.nextLine().trim();

        if (filterValue.isEmpty()) {
            System.out.println("No filter value entered.");
            return;
        }

        JobIterator iterator = collection.createFilteredIterator(
            filterType,
            filterValue
        );

        int count = 0;
        while (iterator.hasNext()) printJob(++count, iterator.next());
        if (count == 0) System.out.println("No jobs matched your filter.");
    }

    private void applyForJob() {
        System.out.println("\n--- Apply Now ---");

        if (student.getResume() == null) {
            System.out.println(
                "No resume on file. Please update your profile first."
            );
            return;
        }

        Set<String> appliedJobIds = portal.getApplicationsForStudent(student.getUserId())
            .stream()
            .map(app -> {
                ApplicationComponent c = app;
                while (c instanceof ApplicationDecorator)
                    c = ((ApplicationDecorator) c).getWrappedComponent();
                return ((BaseApplication) c).getJob().getJobId();
            })
            .collect(Collectors.toSet());

        List<JobPosting> jobs = portal.getJobPostings().stream()
            .filter(j -> !appliedJobIds.contains(j.getJobId()))
            .collect(Collectors.toList());

        if (jobs.isEmpty()) {
            System.out.println("No new jobs available to apply for.");
            return;
        }

        for (int i = 0; i < jobs.size(); i++) printJob(i + 1, jobs.get(i));

        System.out.print("\nSelect Job to apply (0 to cancel): ");
        int idx = parseIndex(scanner.nextLine().trim(), jobs.size());
        if (idx < 0) {
            System.out.println("Cancelled. Select correct Job Application.");
            return;
        }

        JobPosting selectedJob = jobs.get(idx - 1);

        String appId =
            "APP-" + student.getUserId() + "-" + System.currentTimeMillis();
        BaseApplication base = new BaseApplication(
            appId,
            student,
            selectedJob,
            student.getResume()
        );
        base.submit();
        
        student.addApplication(base);
        portal.addApplication(base);
        portal.notifyEvent("APPLICATION_SUBMITTED", base);
        System.out.println("\nApplied! " + base.getDescription());
    }

    private void viewApplications() {
        System.out.println("\n--- My Applications ---");
        List<ApplicationComponent> apps = portal.getApplicationsForStudent(
            student.getUserId()
        );
        if (apps.isEmpty()) {
            System.out.println("No applications yet.");
            return;
        }

        System.out.println("INDEX ||       APPLICATION                ||   STRENGTH");
        for (int i = 0; i < apps.size(); i++) {
            ApplicationComponent application = apps.get(i);
            System.out.println(i + 1 + " || " + application.getDescription() + " || " + application.getStrength());
        }

        System.out.print(
            "\nWithdraw an application? Enter number (0 to skip): "
        );
        int idx = parseIndex(scanner.nextLine().trim(), apps.size());
        if (idx > 0) {
            ApplicationComponent appToRemove = apps.get(idx - 1);
            ApplicationComponent base = appToRemove;
            while (base instanceof ApplicationDecorator) {
                base = ((ApplicationDecorator) base).getWrappedComponent();
            }
            ((BaseApplication) base).withdraw();
            portal.removeApplication(appToRemove);
            System.out.println("Application withdrawn.");
        }
    }

    private void viewProfile() {
        System.out.println("\n=========== Student Profile ==============");
        System.out.println(student);
        System.out.println();
        if (student.getResume() != null) {
            System.out.println(student.getResume());
        } else {
            System.out.println("Resume is not yet built.");
        }

        System.out.println("\n1. Update Profile");
        System.out.println("0. Back");
        System.out.print("Choose: ");
        if ("1".equals(scanner.nextLine().trim())) {
            updateProfile();
        }
    }

    private void updateProfile() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Update Profile ---");
            System.out.println(
                "1. Major         (current: " + student.getMajor() + ")"
            );
            System.out.println(
                "2. GPA           (current: " + student.getGpa() + ")"
            );
            System.out.println(
                "3. Graduation Year (current: " +
                    student.getGraduationYear() +
                    ")"
            );
            System.out.println("4. Resume");
            System.out.println("0. Done");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    System.out.print("New major: ");
                    String major = scanner.nextLine().trim();
                    if (!major.isEmpty()) {
                        student.setMajor(major);
                        System.out.println("Major updated.");
                    }
                }
                case "2" -> {
                    System.out.print("New GPA: ");
                    try {
                        double gpa = Double.parseDouble(
                            scanner.nextLine().trim()
                        );
                        if (gpa >= 0.0 && gpa <= 4.0) {
                            student.setGpa(gpa);
                            System.out.println("GPA updated.");
                        } else System.out.println(
                            "GPA must be between 0.0 and 4.0."
                        );
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid GPA.");
                    }
                }
                case "3" -> {
                    System.out.print("New graduation year: ");
                    try {
                        int year = Integer.parseInt(scanner.nextLine().trim());
                        student.setGraduationYear(year);
                        System.out.println("Graduation year updated.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid year.");
                    }
                }
                case "4" -> updateResume();
                case "0" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void updateResume() {
        ResumeBuilder builder = new ResumeBuilder();

        if (student.getResume() != null) {
            student.getResume().getSkills().forEach(builder::addSkill);
            student
                .getResume()
                .getExperiences()
                .forEach(builder::addExperience);
            student.getResume().getProjects().forEach(builder::addProject);
            student
                .getResume()
                .getCertifications()
                .forEach(builder::addCertification);
            builder.setEducation(student.getResume().getEducation());
        }

        boolean running = true;
        while (running) {
            System.out.println("\n--- Update Resume ---");
            System.out.println("1. Set Education");
            System.out.println("2. Add Skill");
            System.out.println("3. Add Certifications");
            System.out.println("4. Add Experience");
            System.out.println("5. Add Projects");
            System.out.println("0. Save & Back");
            System.out.print("Choose: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    System.out.print("University: ");
                    String uni = scanner.nextLine().trim();
                    System.out.print("Degree: ");
                    String degree = scanner.nextLine().trim();
                    System.out.print("GPA: ");
                    try {
                        double gpa = Double.parseDouble(
                            scanner.nextLine().trim()
                        );
                        builder.setEducation(new Education(uni, degree, gpa));
                        System.out.println("Education set.");
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid GPA.");
                    }
                }
                case "2" -> {
                    System.out.print("Skills (comma-separated, e.g. Java, Python, SQL): ");
                    String skill = scanner.nextLine().trim();
                    if (!skill.isEmpty()) {
                        builder.addSkill(skill);
                        System.out.println("Skills added.");
                    }
                }
                case "3" -> {
                    System.out.print("Certifications (comma-separated, e.g. AWS, Google Analytics): ");
                    String certificates = scanner.nextLine().trim();
                    if (certificates.isEmpty()) {
                        System.out.println("No certifications entered.");
                    } else {
                        builder.addCertification(certificates);
                        System.out.println("Certifications added.");
                    }
                }
                case "4" -> {
                    System.out.print("Company: ");
                    String company = scanner.nextLine().trim();
                    System.out.print("Role: ");
                    String role = scanner.nextLine().trim();
                    System.out.print("Duration (e.g. Jan 2024 - May 2024): ");
                    String duration = scanner.nextLine().trim();
                    System.out.print("Description: ");
                    String desc = scanner.nextLine().trim();
                    if (company.isEmpty() || role.isEmpty() || duration.isEmpty()) {
                        System.out.println("Company, role, and duration are required.");
                    } else {
                        builder.addExperience(
                            new Experience(company, role, duration, desc)
                        );
                        System.out.println("Experience added.");
                    }
                }
                case "5" -> {
                    System.out.print("Project Name: ");
                    String projectName = scanner.nextLine().trim();
                    System.out.print("Tech Stack (comma-separated, e.g. Java, React, MySQL): ");
                    String technologies = scanner.nextLine().trim();
                    System.out.print("Description: ");
                    String desc = scanner.nextLine().trim();
                    if (projectName.isEmpty()) {
                        System.out.println("Project name is required.");
                    } else {
                        builder.addProject(
                            new Project(projectName, technologies, desc)
                        );
                        System.out.println("Project added.");
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }

        student.setResume(builder.build());
        System.out.println("Resume saved.");
    }

    private void getRecommendations() {
        System.out.println("\n--- Job Recommendations ---");
        System.out.println("Match strategy:");
        System.out.println(
            "1. Skill Match     (resume skills ↔ job requirements)"
        );
        System.out.println(
            "2. Major Match     (your major ↔ preferred majors)"
        );
        System.out.println(
            "3. Location Match  (preferred location ↔ job location)"
        );
        System.out.print("Choose: ");

        RecommendationStrategy strategy;
        switch (scanner.nextLine().trim()) {
            case "2" -> strategy = new MajorMatchStrategy(student.getMajor());
            case "3" -> {
                System.out.print("Preferred location: ");
                String loc = scanner.nextLine().trim();
                strategy = new LocationMatchStrategy(loc);
            }
            default -> strategy = new SkillMatchStrategy();
        }

        RecommendationEngine engine = new RecommendationEngine(strategy);
        List<JobPosting> results = engine.recommend(
            student,
            portal.getJobPostings()
        );

        if (results.isEmpty()) {
            System.out.println(
                "No strong matches found. Try a different strategy or update your profile."
            );
        } else {
            System.out.println("\nTop picks for you:");
            for (int i = 0; i < results.size(); i++) printJob(
                i + 1,
                results.get(i)
            );
        }
    }

    private int parseIndex(String input, int size) {
        try {
            int n = Integer.parseInt(input);
            return (n >= 1 && n <= size) ? n : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void printJob(int idx, JobPosting job) {
        System.out.println(
            idx +
                " | " +
                job.getTitle() +
                " | " +
                job.getCompany() +
                " | " +
                job.getType() +
                " | " +
                job.getLocation()
        );
    }

    private ApplicationComponent promptDecorators(ApplicationComponent app) {
        System.out.println("\nEnhance your application (y/n):");

        System.out.print("  Add Cover Letter?        ");
        if (isYes()) {
            System.out.print("  Enter cover letter text: ");
            String text = scanner.nextLine().trim();
            app = new CoverLetterDecorator(app, text);
        }

        System.out.print("  Add Portfolio link?      ");
        if (isYes()) {
            System.out.print("  Enter portfolio URL: ");
            String url = scanner.nextLine().trim();
            app = new PortfolioDecorator(app, url);
        }

        System.out.print("  Add Referral?            ");
        if (isYes()) {
            System.out.print("  Referrer name: ");
            String name = scanner.nextLine().trim();
            System.out.print("  Referrer email: ");
            String email = scanner.nextLine().trim();
            System.out.print("  Referrer title: ");
            String title = scanner.nextLine().trim();
            app = new ReferralDecorator(app, name, email, title);
        }

        System.out.print("  Add Recommendation?      ");
        if (isYes()) {
            System.out.print("  Recommender name: ");
            String name = scanner.nextLine().trim();
            System.out.print("  Letter content: ");
            String content = scanner.nextLine().trim();
            app = new RecommendationDecorator(app, name, content);
        }

        return app;
    }

    private boolean isYes() {
        String option = scanner.nextLine().trim().toLowerCase();
        if (option.equals("y") || option.equals("yes")) return true;
        if (option.equals("n") || option.equals("no"))  return false;
        System.out.println("Invalid option");
        return false;
    }

    private void enhanceApplication() {
        System.out.println("\n=== Enhance Application ===");
        List<BaseApplication> apps = student.getApplications();
        if (apps.isEmpty()) {
            System.out.println("No applications to enhance.");
            return;
        }
        for (int i = 0; i < apps.size(); i++) {
            System.out.println((i + 1) + ". " + apps.get(i).getDescription() + " | Strength: " + apps.get(i).getStrength());
        }
        System.out.print("Select application to enhance: ");
        int choice = parseIndex(scanner.nextLine().trim(), apps.size());
        if (choice < 0) return;

        BaseApplication app = apps.get(choice - 1);
        System.out.println("Add: 1) Cover Letter  2) Portfolio  3) Referral  4) Recommendation");
        System.out.print("Choice: ");
        String opt = scanner.nextLine().trim();
        ApplicationComponent enhanced;
        switch (opt) {
            case "1":
                System.out.print("Enter cover letter text: ");
                enhanced = new CoverLetterDecorator(app, scanner.nextLine());
                break;
            case "2":
                System.out.print("Enter portfolio URL: ");
                enhanced = new PortfolioDecorator(app, scanner.nextLine());
                break;
            case "3":
                System.out.print("Referrer name: ");
                String rName = scanner.nextLine();
                System.out.print("Referrer email: ");
                String rEmail = scanner.nextLine();
                System.out.print("Referrer title: ");
                String rTitle = scanner.nextLine();
                enhanced = new ReferralDecorator(app, rName, rEmail, rTitle);
                break;
            case "4":
                System.out.print("Recommender name: ");
                String recName = scanner.nextLine();
                System.out.print("Letter content: ");
                String content = scanner.nextLine();
                enhanced = new RecommendationDecorator(app, recName, content);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        portal.removeApplication(app);
        portal.addApplication(enhanced);
        System.out.println("Enhanced! " + enhanced.getDescription() + " | Strength: " + enhanced.getStrength());
    }

    private void findMentor() {
        System.out.println("\n=== Find a Mentor ===");
        System.out.println("Mentor matching is planned for a future release.");
    }

    private void viewNotifications() {
        System.out.println("\n=== Notifications ===");
        List<String> notifications = student.getNotifications();
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
