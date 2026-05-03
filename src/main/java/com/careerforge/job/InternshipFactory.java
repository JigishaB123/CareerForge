package com.careerforge.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import com.careerforge.util.SkillUtils;
public class InternshipFactory extends JobPostingFactory{

    /**
     * Creates a FullTimePosting from the provided details map.
     * Required keys : jobId, title, company, location, deadline (yyyy-MM-dd)
     *                  requiredSkills (comma-separated), status, salaryMin,
     *                 salaryMax, hasBenefits, signingBonus
     * @throws RuntimeException if required fields are missing, deadline format is invalid,
     *                          or salaryMin is greater than salaryMax
     */
    @Override
    public JobPosting createPosting(Map<String, String> details) {
        try {
            String jobId = details.get("jobId");
            Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(details.get("deadline"));
            String duration  = details.getOrDefault("duration", "3");
            double stipend = Double.parseDouble(details.getOrDefault("stipend", "0"));
            boolean acadCredit = Boolean.parseBoolean(details.getOrDefault("academicCredit", "false"));

            return new InternshipPosting(
                jobId,
                details.get("title"),
                details.get("company"),
                details.get("location"),
                SkillUtils.normalizeSkills(java.util.List.of(details.getOrDefault("requiredSkills", ""))),
                details.get("preferredMajor"),
                deadline,
                details.getOrDefault("status", "Open"),
                duration, stipend, acadCredit
            );
        } catch (Exception e) {
            throw new RuntimeException("InternshipFactory: failed to create posting — " + e.getMessage());
        }
    }

}
