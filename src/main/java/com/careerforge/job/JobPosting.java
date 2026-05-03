package com.careerforge.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
public interface JobPosting {
    public String getJobId();
    public String getTitle();
    public String getCompany();
    public String getLocation();
    public List<String> getRequiredSkills();
    public String getPreferredMajor();
    public Date getDeadline();
    public String getStatus();
    public String getType();

    default void display() {
        System.out.println("-----------------------------");
        System.out.println("ID       : " + getJobId());
        System.out.println("Title    : " + getTitle());
        System.out.println("Company  : " + getCompany());
        System.out.println("Location : " + getLocation());
        System.out.println("Type     : " + getType());
        System.out.println("Skills   : " + String.join(", ", getRequiredSkills()));
        System.out.println("Deadline : " + new SimpleDateFormat("yyyy-MM-dd").format(getDeadline()));
        System.out.println("Status   : " + getStatus());
    }

}
