package com.careerforge.application;

import com.careerforge.application.BaseApplication;

public class DraftState implements ApplicationState {

    @Override
    public void submit(BaseApplication app) {
        System.out.println("Application " + app.getApplicationId() + " submitted.");
        app.setState(new SubmittedState());
        app.addStatusEntry("Draft -> Submitted");
    }

    @Override
    public void withdraw(BaseApplication app) {
        System.out.println("Application " + app.getApplicationId() + " withdrawn from draft.");
        app.setState(new WithdrawnState());
        app.addStatusEntry("Draft -> Withdrawn");
    }

    @Override
    public String getStateName() {
        return "Draft";
    }
    
}
