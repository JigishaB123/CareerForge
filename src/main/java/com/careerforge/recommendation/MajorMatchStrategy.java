package com.careerforge.recommendation;

import com.careerforge.application.BaseApplication;
/**
 * A concrete strategy that checks if the applicant's major matches a target major.
 */
public class MajorMatchStrategy implements RecommendationStrategy {
    private String targetMajor;

    public MajorMatchStrategy(String targetMajor) {
        this.targetMajor = targetMajor;
    }

    @Override
    public int calculateScore(BaseApplication application) {
        // Student student = application.getStudent();
        String preferredMajor = application.getJob().getPreferredMajor();
        // A simple check. This could be expanded to include related majors.
        return preferredMajor != null && preferredMajor.equalsIgnoreCase(targetMajor) ? 100 : 0;
    }
}