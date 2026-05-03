package com.careerforge.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private ValidationUtils() {}

    public static List<String> validateStudentRegistration(
            String firstName,
            String lastName,
            String email,
            String password,
            String confirmPassword,
            boolean emailTaken
    ) {
        List<String> errors = new ArrayList<>();
        requireText(firstName, "First name", errors);
        requireText(lastName, "Last name", errors);
        requireEmail(email, errors);
        requirePassword(password, errors);
        if (!password.equals(confirmPassword)) {
            errors.add("Password and confirm password must match.");
        }
        if (emailTaken) {
            errors.add("An account with this email already exists.");
        }
        return errors;
    }

    public static List<String> validateProfile(String major, String gpaText, String yearText) {
        List<String> errors = new ArrayList<>();
        requireText(major, "Major", errors);
        requireGpa(gpaText, errors);
        requireGraduationYear(yearText, errors);
        return errors;
    }

    public static List<String> validateJobPosting(
            String title,
            String location,
            String skills,
            String preferredMajor,
            String deadline,
            String jobType,
            String minSalary,
            String maxSalary,
            String signingBonus,
            String stipend,
            String duration,
            String rotationCycle,
            String durationMonths
    ) {
        List<String> errors = new ArrayList<>();
        requireText(title, "Job title", errors);
        requireText(location, "Location", errors);
        requireText(skills, "Required skills", errors);
        requireText(preferredMajor, "Preferred major", errors);
        requireFutureDate(deadline, "Application deadline", errors);

        switch (jobType) {
            case "Full-Time" -> {
                requirePositiveNumber(minSalary, "Minimum salary", errors);
                requirePositiveNumber(maxSalary, "Maximum salary", errors);
                requirePositiveNumber(signingBonus, "Signing bonus", errors);
                if (errors.isEmpty()) {
                    double min = Double.parseDouble(minSalary.trim());
                    double max = Double.parseDouble(maxSalary.trim());
                    if (min > max) {
                        errors.add("Minimum salary cannot be greater than maximum salary.");
                    }
                }
            }
            case "Internship" -> {
                requirePositiveInteger(duration, "Internship duration in months", errors);
                requirePositiveNumber(stipend, "Stipend", errors);
            }
            case "Co-Op" -> {
                requireText(rotationCycle, "Rotation cycle", errors);
                requirePositiveInteger(durationMonths, "Duration in months", errors);
            }
            default -> errors.add("Select a valid job type.");
        }
        return errors;
    }

    public static void requireText(String value, String label, List<String> errors) {
        if (value == null || value.trim().isEmpty()) {
            errors.add(label + " is required.");
        }
    }

    public static void requireEmail(String email, List<String> errors) {
        if (email == null || email.trim().isEmpty()) {
            errors.add("Email is required.");
            return;
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            errors.add("Enter a valid email address.");
        }
    }

    static void requirePassword(String password, List<String> errors) {
        if (password == null || password.isBlank()) {
            errors.add("Password is required.");
        } else if (password.trim().length() < 6) {
            errors.add("Password must be at least 6 characters.");
        }
    }

    static void requireGpa(String gpaText, List<String> errors) {
        try {
            double gpa = Double.parseDouble(gpaText.trim());
            if (gpa < 0.0 || gpa > 4.0) {
                errors.add("GPA must be between 0.0 and 4.0.");
            }
        } catch (Exception ex) {
            errors.add("GPA must be a valid number.");
        }
    }

    static void requireGraduationYear(String yearText, List<String> errors) {
        try {
            int year = Integer.parseInt(yearText.trim());
            int currentYear = LocalDate.now().getYear();
            if (year < currentYear - 1 || year > currentYear + 10) {
                errors.add("Graduation year must be realistic.");
            }
        } catch (Exception ex) {
            errors.add("Graduation year must be a valid number.");
        }
    }

    static void requireDate(String dateText, String label, List<String> errors) {
        try {
            LocalDate.parse(dateText.trim());
        } catch (DateTimeParseException ex) {
            errors.add(label + " must be in YYYY-MM-DD format.");
        }
    }

    static void requireFutureDate(String dateText, String label, List<String> errors) {
        try {
            LocalDate date = LocalDate.parse(dateText.trim());
            if (!date.isAfter(LocalDate.now())) {
                errors.add(label + " must be after today.");
            }
        } catch (DateTimeParseException ex) {
            errors.add(label + " must be in YYYY-MM-DD format.");
        }
    }

    static void requirePositiveNumber(String value, String label, List<String> errors) {
        try {
            double number = Double.parseDouble(value.trim());
            if (number < 0) {
                errors.add(label + " must be 0 or greater.");
            }
        } catch (Exception ex) {
            errors.add(label + " must be a valid number.");
        }
    }

    static void requirePositiveInteger(String value, String label, List<String> errors) {
        try {
            int number = Integer.parseInt(value.trim());
            if (number <= 0) {
                errors.add(label + " must be greater than 0.");
            }
        } catch (Exception ex) {
            errors.add(label + " must be a valid whole number.");
        }
    }
}
