package com.careerforge.recommendation;

import com.careerforge.application.BaseApplication;
import com.careerforge.job.JobPosting;
import com.careerforge.user.Student;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The Context class that uses a RecommendationStrategy to score and recommend applications.
 * The strategy can be changed at runtime to alter the recommendation logic.
 */
public class RecommendationEngine {
    private RecommendationStrategy strategy;

    public RecommendationEngine(RecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(RecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    public List<JobPosting> recommend(Student student, List<JobPosting> jobs) {
        System.out.println("\n--- Running Recommendation Engine with " + strategy.getClass().getSimpleName() + " ---");
        
        

        List<JobPosting> results = jobs.stream()
                .map(job -> {
                    BaseApplication temp = new BaseApplication(
                            "temp", student, job, student.getResume()); 
                    int score = strategy.calculateScore(temp);
                    return new AbstractMap.SimpleEntry<>(job, score);
                })
                .filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<JobPosting, Integer>comparingByValue().reversed())
                .peek(e -> System.out.printf("Score: %-3d | %s @ %s%n",
                        e.getValue(),
                        e.getKey().getTitle(),
                        e.getKey().getLocation()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        System.out.println("--- Engine finished ---");
        return results;
    }
}