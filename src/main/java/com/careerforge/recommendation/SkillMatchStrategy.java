package com.careerforge.recommendation;

import java.util.List;
import com.careerforge.resume.Resume;
import com.careerforge.application.BaseApplication;
import com.careerforge.job.JobPosting;
import com.careerforge.util.SkillUtils;

/**
 * A concrete strategy that calculates a match score based on the number
 * of matching skills between the resume and the job requirements.
 */
public class SkillMatchStrategy implements RecommendationStrategy {

    @Override
    public int calculateScore(BaseApplication application) {
        Resume resume = application.getResume();
        JobPosting job = application.getJob();

        List<String> studentSkills   = resume.getSkills();
        List<String> requiredSkills  = job.getRequiredSkills();
        List<String> normalizedStudentSkills = SkillUtils.normalizeSkills(studentSkills);
        List<String> normalizedRequiredSkills = SkillUtils.normalizeSkills(requiredSkills);

        if (normalizedStudentSkills.isEmpty() || normalizedRequiredSkills.isEmpty()) {
            return 0;
        }

        long matchCount = normalizedRequiredSkills.stream()
                .filter(req -> normalizedStudentSkills.stream()
                        .anyMatch(s -> s.trim().equalsIgnoreCase(req.trim())))
                .count();

        return (int) ((double) matchCount / normalizedRequiredSkills.size() * 100);
    }
}
