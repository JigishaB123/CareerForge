package com.careerforge.application;

import com.careerforge.application.BaseApplication;

public class SubmittedState implements ApplicationState {

    @Override
    public void review(BaseApplication app) {
        System.out.println("Application " + app.getApplicationId() + " is now under review.");
        app.setState(new UnderReviewState());
        app.addStatusEntry("Submitted -> UnderReview");
    }

    @Override
    public void withdraw(BaseApplication app) {
        System.out.println("Application " + app.getApplicationId() + " withdrawn.");
        app.setState(new WithdrawnState());
        app.addStatusEntry("Submitted -> Withdrawn");
    }

    @Override
    public void reject(BaseApplication app) {
        System.out.println("Application " + app.getApplicationId() + " rejected.");
        app.setState(new RejectedState());
        app.addStatusEntry("Submitted -> Rejected");
    }

    @Override
    public String getStateName() {
        return "Submitted";
    }
    
}
