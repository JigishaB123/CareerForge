package com.careerforge.recommendation;

import com.careerforge.application.BaseApplication;
import com.careerforge.job.JobPosting;

/**
 * A concrete strategy that checks if the job's location matches a preferred location.
 * It gives a high score for "Remote" jobs as a generally good match.
 */
public class LocationMatchStrategy implements RecommendationStrategy {
    private String preferredLocation;

    public LocationMatchStrategy(){}

    public LocationMatchStrategy(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    @Override
    public int calculateScore(BaseApplication application) {
        JobPosting job = application.getJob();
        if (job.getLocation() == null) return 0;
        if (preferredLocation == null || preferredLocation.isBlank()) return 0;
        if (job.getLocation().equalsIgnoreCase(preferredLocation)) return 100;
        if (preferredLocation.equalsIgnoreCase("Remote") && job.getLocation().equalsIgnoreCase("Remote")) return 100;
        return 0;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }
}
