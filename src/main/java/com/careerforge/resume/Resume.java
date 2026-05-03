package com.careerforge.resume;

import java.util.ArrayList;
import java.util.List;
public class Resume {
    private Education education;
    private List<String> skills;
    private List<Experience> experiences;
    private List<Project> projects;
    private List<String> certifications;

    Resume() {
        this.skills         = new ArrayList<>();
        this.experiences    = new ArrayList<>();
        this.projects       = new ArrayList<>();
        this.certifications = new ArrayList<>();
    }

    void setEducation(Education education)        { this.education = education; }
    void addSkill(String skill) { 
        for (String s : skill.split(",")) {
            String trimmed = s.trim();
            if (!trimmed.isEmpty()) this.skills.add(trimmed);
        } 
}
    void addExperience(Experience experience)     { this.experiences.add(experience); }
    void addProject(Project project)              { this.projects.add(project); }
    void addCertification(String certification) { 
        for (String c : certification.split(",")) {
            String trimmed = c.trim();
            if (!trimmed.isEmpty()) this.certifications.add(trimmed);
        }
 }

    public Education getEducation()              { return education; }
    public List<String> getSkills()              { return skills; }
    public List<Experience> getExperiences()     { return experiences; }
    public List<Project> getProjects()           { return projects; }
    public List<String> getCertifications()      { return certifications; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=================== RESUME ===================\n");
        sb.append("EDUCATION   : ").append(education != null ? education : "N/A").append("\n\n");
        sb.append("SKILLS      : ").append(skills.isEmpty() ? "None" : String.join(", ", skills)).append("\n\n");
        sb.append("EXPERIENCE  :\n");
        if (experiences.isEmpty()) {
            sb.append("  None\n");
        } else {
            for (Experience e : experiences) sb.append("  - ").append(e).append("\n");
        }
        sb.append("\nPROJECTS    :\n");
        if (projects.isEmpty()) {
            sb.append("  None\n");
        } else {
            for (Project p : projects) sb.append("  - ").append(p).append("\n");
        }
        sb.append("\nCERTIFICATIONS: ").append(certifications.isEmpty() ? "None" : String.join(", ", certifications));
        return sb.toString();
    }
}
