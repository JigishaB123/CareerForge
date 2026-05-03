package com.careerforge.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import com.careerforge.util.SkillUtils;

public class CoOpFactory extends JobPostingFactory{

    /**
     * Creates a CoOpPosting from the provided details map.
     * Required keys : jobId, title, company, location, deadline (yyyy-MM-dd)
     *                  requiredSkills (comma-separated), status, rotationCycle,
     *                 durationMonths
     * @throws RuntimeException if required fields are missing or deadline format is invalid
     */
    @Override
    public JobPosting createPosting(Map<String, String> details) {
        try {
            String jobId = details.get("jobId");
            Date deadline = new SimpleDateFormat("yyyy-MM-dd").parse(details.get("deadline"));
            String rotationCycle = details.getOrDefault("rotationCycle", "6 months");
            int durationMonths  = Integer.parseInt(details.getOrDefault("durationMonths", "6"));

            return new CoOpPosting(
                jobId,
                details.get("title"),
                details.get("company"),
                details.get("location"),
                SkillUtils.normalizeSkills(java.util.List.of(details.getOrDefault("requiredSkills", ""))),
                details.get("preferredMajor"),
                deadline,
                details.getOrDefault("status", "Open"),
                rotationCycle, durationMonths
            );
        } catch (Exception e) {
            throw new RuntimeException("CoOpFactory: failed to create posting — " + e.getMessage());
        }
    }
}
