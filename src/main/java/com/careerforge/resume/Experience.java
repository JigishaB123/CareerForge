package com.careerforge.resume;

public class Experience {
    private String company;
    private String role;
    private String duration;
    private String description;
    
    public Experience(String company, String role, String duration, String description) {
        this.company = company;
        this.role = role;
        this.duration = duration;
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public String getRole() {
        return role;
    }

    public String getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return role + ", " + company + " (" + duration + "): " + description;
    }
}
