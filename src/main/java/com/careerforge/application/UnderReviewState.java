package com.careerforge.application;

import com.careerforge.application.BaseApplication;

public class UnderReviewState implements ApplicationState  {
    
    @Override
    public void scheduleInterview(BaseApplication app) {
        System.out.println("Interview scheduled for application " + app.getApplicationId() + ".");
        app.setState(new InterviewState());
        app.addStatusEntry("UnderReview -> Interview");
    }

    @Override
    public void reject(BaseApplication app) {
        System.out.println("Application " + app.getApplicationId() + " rejected during review.");
        app.setState(new RejectedState());
        app.addStatusEntry("UnderReview -> Rejected");
    }

    @Override
    public void withdraw(BaseApplication app) {
        System.out.println("Application " + app.getApplicationId() + " withdrawn.");
        app.setState(new WithdrawnState());
        app.addStatusEntry("UnderReview -> Withdrawn");
    }

    @Override
    public String getStateName() {
        return "UnderReview";
    }
}
