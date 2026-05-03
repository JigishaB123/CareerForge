package com.careerforge.job;

import java.util.Date;
import java.util.List;

public class CoOpPosting implements JobPosting{
    private String jobId;
    private String title;
    private String company;
    private String location;
    private List<String> requiredSkills;
    private String preferredMajor;
    private Date deadline;
    private String status;
    private String rotationCycle;
    private int durationMonths;

    public CoOpPosting(String jobId, String title, String company, String location,
        List<String> requiredSkills, String preferredMajor, Date deadline, String status,
        String rotationCycle, int durationMonths) {
        this.jobId = jobId;
        this.title = title;
        this.company = company;
        this.location = location;
        this.requiredSkills = requiredSkills;
        this.preferredMajor = preferredMajor;
        this.deadline = deadline;
        this.status = status;
        this.rotationCycle = rotationCycle;
        this.durationMonths = durationMonths;
    }

    @Override public String getJobId()               { return jobId; }
    @Override public String getTitle()               { return title; }
    @Override public String getCompany()             { return company; }
    @Override public String getLocation()            { return location; }
    @Override public List<String> getRequiredSkills(){ return requiredSkills; }
    @Override public Date getDeadline()            { return deadline; }
    @Override public String getStatus()              { return status; }
    @Override public String getType()                { return "Co-Op"; }
    @Override public String getPreferredMajor()                { return preferredMajor; }

    public String getRotationCycle()  { return rotationCycle; }
    public int getDurationMonths()    { return durationMonths; }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void display() {
        JobPosting.super.display();
        System.out.println("Rotation : " + rotationCycle);
        System.out.println("Duration : " + durationMonths + " months");
    }
}
