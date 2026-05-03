package com.careerforge.recommendation;

import com.careerforge.application.BaseApplication;

/**
 * The Strategy interface for defining different algorithms to calculate
 * an application's match score.
 */
public interface RecommendationStrategy {
    int calculateScore(BaseApplication application);
}