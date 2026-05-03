package com.careerforge.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import com.careerforge.util.SkillUtils;

public class FullTimeFactory extends JobPostingFactory{
    @Override
    /**
     * Creates a FullTimePosting from the provided details map.
     * Required keys : jobId, title, company, location, deadline (yyyy-MM-dd), 
     *                  requiredSkills (comma-separated), status, salaryMin,
     *                 salaryMax, hasBenefits, signingBonus
     * @throws RuntimeException if required fields are missing, deadline format is invalid,
     *                          or salaryMin is greater than salaryMax
     */
    public JobPosting createPosting(Map<String, String> details) {
        try {
            String jobId = details.get("jobId");
            Date deadline  = new SimpleDateFormat("yyyy-MM-dd").parse(details.get("deadline"));
            double salaryMin = Double.parseDouble(details.getOrDefault("salaryMin", "0"));
            double salaryMax = Double.parseDouble(details.getOrDefault("salaryMax", "0"));
            boolean hasBenefits = Boolean.parseBoolean(details.getOrDefault("hasBenefits", "false"));
            double signingBonus = Double.parseDouble(details.getOrDefault("signingBonus", "0"));

            if (salaryMin > salaryMax) {
                throw new IllegalArgumentException("salaryMin (" + salaryMin + ") cannot be greater than salaryMax (" + salaryMax + ")");
            }

            return new FullTimePosting(
                jobId,
                details.get("title"),
                details.get("company"),
                details.get("location"),
                SkillUtils.normalizeSkills(java.util.List.of(details.getOrDefault("requiredSkills", ""))),
                details.get("preferredMajor"),
                deadline,
                details.getOrDefault("status", "Open"),
                salaryMin, salaryMax, hasBenefits, signingBonus
            );
        } catch (Exception e) {
            throw new RuntimeException("FullTimeFactory: failed to create posting — " + e.getMessage());
        }
    }
}
