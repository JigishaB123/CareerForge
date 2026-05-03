package com.careerforge.job;

import java.util.Date;
import java.util.List;
public class InternshipPosting implements JobPosting{
    private String jobId;
    private String title;
    private String company;
    private String location;
    private List<String> requiredSkills;
    private String preferredMajor;
    private Date deadline;
    private String status;
    private String duration;
    private double stipend;
    private boolean academicCredit;

    public InternshipPosting(String jobId, String title, String company, String location,
        List<String> requiredSkills, String preferredMajor, Date deadline, String status,
        String duration, double stipend, boolean academicCredit) {
        this.jobId = jobId;
        this.title = title;
        this.company = company;
        this.location = location;
        this.requiredSkills = requiredSkills;
        this.preferredMajor = preferredMajor;
        this.deadline = deadline;
        this.status = status;
        this.duration = duration;
        this.stipend = stipend;
        this.academicCredit = academicCredit;
    }

    @Override
    public String getJobId() { return jobId; }

    @Override
    public String getTitle() { return title; }

    @Override
    public String getCompany() { return company; }

    @Override
    public String getLocation() { return location; }

    @Override
    public List<String> getRequiredSkills() { return requiredSkills; }

    @Override
    public Date getDeadline() { return deadline; }

    @Override
    public String getStatus() { return status; }

    @Override
    public String getType() { return "Internship"; }

    @Override public String getPreferredMajor()      { return preferredMajor; }

    public String getDuration() { return duration; }

    public double getStipend() { return stipend; }

    public boolean isAcademicCredit() { return academicCredit; }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAcademicCredit(boolean academicCredit) {
        this.academicCredit = academicCredit;
    }
    
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @Override
    public void display() {
        JobPosting.super.display();
        System.out.println("Duration : " + duration + " months");
        System.out.println("Stipend  : $" + stipend + "/month");
        System.out.println("Academic Credit: " + (academicCredit ? "Yes" : "No"));
    }

}
