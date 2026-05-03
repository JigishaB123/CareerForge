package com.careerforge.job;

import java.util.Date;
import java.util.List;

public class FullTimePosting implements JobPosting{
    private String jobId;
    private String title;
    private String company;
    private String location;
    private List<String> requiredSkills;
    private String preferredMajor;
    private Date deadline;
    private String status;
    private double salaryMin;
    private double salaryMax;
    private boolean hasBenefits;
    private double signingBonus;

    public FullTimePosting(String jobId, String title, String company, String location,
        List<String> requiredSkills, String preferredMajor, Date deadline, String status,
        double salaryMin, double salaryMax,
        boolean hasBenefits, double signingBonus) {
        this.jobId = jobId;
        this.title = title;
        this.company = company;
        this.location = location;
        this.requiredSkills = requiredSkills;
        this.preferredMajor = preferredMajor;
        this.deadline = deadline;
        this.status = status;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.hasBenefits = hasBenefits;
        this.signingBonus = signingBonus;
    }

    @Override public String getJobId()               { return jobId; }
    @Override public String getTitle()               { return title; }
    @Override public String getCompany()             { return company; }
    @Override public String getLocation()            { return location; }
    @Override public List<String> getRequiredSkills(){ return requiredSkills; }
    @Override public Date getDeadline()            { return deadline; }
    @Override public String getStatus()              { return status; }
    @Override public String getType()                { return "Full-Time"; }
    @Override public String getPreferredMajor()      { return preferredMajor; }

    public double getSalaryMin()   { return salaryMin; }
    public double getSalaryMax()   { return salaryMax; }
    public boolean isHasBenefits() { return hasBenefits; }
    public double getSigningBonus(){ return signingBonus; }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void display() {
        JobPosting.super.display();
        System.out.println("Salary   : $" + salaryMin + " - $" + salaryMax);
        System.out.println("Benefits : " + (hasBenefits ? "Yes" : "No"));
        System.out.println("Signing Bonus: $" + signingBonus);
    }
}
