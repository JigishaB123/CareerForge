package com.careerforge.resume;

public class Education {
    private String university;
    private String degree;
    private double gpa;
    
    public Education(String university, String degree, double gpa) {
        this.university = university;
        this.degree = degree;
        this.gpa = gpa;
    }

    public String getUniversity() {
        return university;
    }

    public String getDegree() {
        return degree;
    }

    public double getGpa() {
        return gpa;
    }

    @Override
    public String toString() {
        return degree + ", " + university + " (GPA: " + gpa + ")";
    }
}
