package com.careerforge.user;

public class Admin extends User {

    private String role;

    public Admin(String userId, String firstName, String lastName, String email, String password, String role) {
        super(userId, firstName, lastName, email, password);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void viewDashboard() {
        System.out.println("[Admin: " + firstName + " " + lastName + "] Opening dashboard...");
    }

    public void generateReport() {
        System.out.println("[Admin: " + firstName + " " + lastName + "] Generating report...");
    }

    @Override
    public String toString() {
        return super.toString() + " | Admin role: " + role;
    }
}
