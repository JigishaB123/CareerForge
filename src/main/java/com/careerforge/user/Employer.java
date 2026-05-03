package com.careerforge.user;

import java.util.ArrayList;
import java.util.List;
import com.careerforge.job.JobPosting;
public class Employer extends User{

    private String companyName;
    private List<JobPosting> postedJobs;
    private List<String> notifications = new ArrayList<>();

    public Employer(String userId, String firstName, String lastName, String email, String password,
                    String companyName) {
        super(userId, firstName, lastName, email, password);
        this.companyName = companyName;
        this.postedJobs = new ArrayList<>();
    }

    public Employer(String userId, String firstName, String lastName, String email, String password) {
        super(userId, firstName, lastName, email, password);
        this.companyName = "";
        this.postedJobs = new ArrayList<>();
    }

    public String getCompanyName() {
        return companyName;
    }
    
    public List<JobPosting> getPostedJobs() {
        if (postedJobs == null) {
            postedJobs = new ArrayList<>();
        }
        return postedJobs;
    }

    public void postJob(JobPosting job) {
        if (postedJobs == null) {
            postedJobs = new ArrayList<>();
        }
        postedJobs.add(job);
    }

    public void addNotification(String message) {
        if (notifications == null)
            notifications = new ArrayList<>();
        notifications.add(message);
    }

    public List<String> getNotifications() {
        if (notifications == null)
            notifications = new ArrayList<>();
        return notifications;
    }

    @Override
    public String toString() {
        return super.toString() + " | Company: " + companyName;
    }

}
