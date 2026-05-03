package com.careerforge.application;

import com.careerforge.application.BaseApplication;

public class InterviewState implements ApplicationState {

    @Override
    public void makeOffer(BaseApplication app) {
        System.out.println("Offer extended for application " + app.getApplicationId() + ".");
        app.setState(new OfferState());
        app.addStatusEntry("Interview -> Offer");
    }

    @Override
    public void reject(BaseApplication app) {
        System.out.println("Application " + app.getApplicationId() + " rejected after interview.");
        app.setState(new RejectedState());
        app.addStatusEntry("Interview -> Rejected");
    }

    @Override
    public void withdraw(BaseApplication app) {
        System.out.println("Application " + app.getApplicationId() + " withdrawn.");
        app.setState(new WithdrawnState());
        app.addStatusEntry("Interview -> Withdrawn");
    }

    @Override
    public String getStateName() {
        return "Interview";
    }
    
}
