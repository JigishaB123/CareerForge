package com.careerforge.resume;

public class ResumeBuilder {
    private Resume resume;

    public ResumeBuilder() {
        this.resume = new Resume();
    }

    public ResumeBuilder setEducation(Education education) {
        resume.setEducation(education);
        return this;
    }

    public ResumeBuilder addSkill(String skill) {
        resume.addSkill(skill);
        return this;
    }

    public ResumeBuilder addExperience(Experience experience) {
        resume.addExperience(experience);
        return this;
    }

    public ResumeBuilder addProject(Project project) {
        resume.addProject(project);
        return this;
    }

    public ResumeBuilder addCertification(String certification) {
        resume.addCertification(certification);
        return this;
    }

    public Resume build() {
        return resume;
    }
}
