package com.careerforge.application;

import java.util.ArrayList;
import java.util.List;

import com.careerforge.resume.Resume;
import com.careerforge.job.JobPosting;
import com.careerforge.user.Student;
import com.careerforge.application.ApplicationState;
import com.careerforge.application.DraftState;

public class BaseApplication implements ApplicationComponent {

    private String applicationId;
    private Student student;
    private JobPosting job;
    private Resume resume;
    private ApplicationState currentState;
    private List<String> statusHistory;

    public BaseApplication(String applicationId, Student student, JobPosting job, Resume resume) {
        this.applicationId = applicationId;
        this.student = student;
        this.job = job;
        this.resume = resume;
        this.currentState = new DraftState();
        this.statusHistory = new ArrayList<>();
        this.statusHistory.add("Application created in Draft state");
    }

    @Override
    public String getDescription() {
        return "Application " + applicationId + " | " + student.getFirstName() + " " 
                + student.getLastName() + " -> " + job.getTitle() + " at " + job.getCompany();
    }

    @Override
    public int getStrength() {
        return 10;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public JobPosting getJob() {
        return job;
    }

    public Resume getResume() {
        return resume;
    }

    public ApplicationState getCurrentState() {
        return currentState;
    }

    public void setState(ApplicationState state) {
        this.currentState = state;
    }

    public void addStatusEntry(String entry) {
        statusHistory.add(entry);
    }

    public List<String> getStatusHistory() {
        return statusHistory;
    }

    public void submit() {
        currentState.submit(this);
    }

    public void review() {
        currentState.review(this);
    }

    public void scheduleInterview() {
        currentState.scheduleInterview(this);
    }

    public void makeOffer() {
        currentState.makeOffer(this);
    }

    public void accept() {
        currentState.accept(this);
    }

    public void reject() {
        currentState.reject(this);
    }

    public void withdraw() {
        currentState.withdraw(this);
    }

    @Override
    public String toString() {
        return getDescription() + " | State: " + currentState.getStateName() 
                + " | Strength: " + getStrength();
    }

}
