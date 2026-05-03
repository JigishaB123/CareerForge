package com.careerforge.application;

import com.careerforge.application.BaseApplication;

public class OfferState implements ApplicationState {

    @Override
    public void accept(BaseApplication app) {
        System.out.println("Offer accepted for application " + app.getApplicationId() + "!");
        app.setState(new AcceptedState());
        app.addStatusEntry("Offer -> Accepted");
    }

    @Override
    public void reject(BaseApplication app) {
        System.out.println("Offer declined for application " + app.getApplicationId() + ".");
        app.setState(new RejectedState());
        app.addStatusEntry("Offer -> Rejected");
    }

    @Override
    public void withdraw(BaseApplication app) {
        System.out.println("Application " + app.getApplicationId() + " withdrawn.");
        app.setState(new WithdrawnState());
        app.addStatusEntry("Offer -> Withdrawn");
    }

    @Override
    public String getStateName() {
        return "Offer";
    }
    
}
