package com.careerforge.user;

import java.util.ArrayList;
import java.util.List;
import com.careerforge.resume.Resume;
import com.careerforge.application.BaseApplication;
public class Student extends User {
    private String major;
    private double gpa;
    private int graduationYear;
    private Resume resume;
    private transient List<BaseApplication> applications;
    private List<String> notifications = new ArrayList<>();

    public Student(String userId, String firstName, String lastName, String email, String password, 
        String major, double gpa, int graduationYear) {
        super(userId, firstName, lastName, email, password);
        this.major = major;
        this.gpa = gpa;
        this.graduationYear = graduationYear;
        this.applications = new ArrayList<>();
    }

    public Student(String userId, String firstName, String lastName, String email, String password) {
        super(userId, firstName, lastName, email, password);
        this.major = "";
        this.gpa = 0.0;
        this.applications = new ArrayList<>();
        this.graduationYear = 0;
    }

    public String getMajor() {
        return major;
    }

    public double getGpa() {
        return gpa;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public Resume getResume() {
        return resume;
    }

    public List<BaseApplication> getApplications() {
        return applications;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public void addApplication(BaseApplication app) {
        if(applications == null)
            applications = new ArrayList<>();
        applications.add(app);
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
        return super.toString() + "Major: " + major
            + "\n GPA: " + gpa + " \n Graduation Year: " + graduationYear;
    }

}
