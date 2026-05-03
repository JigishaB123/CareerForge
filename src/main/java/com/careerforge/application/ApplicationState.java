package com.careerforge.application;

import com.careerforge.application.BaseApplication;

public interface ApplicationState {

    default void submit(BaseApplication app) {
        System.out.println("Invalid action: Cannot submit in " + getStateName() + " state.");
    }

    default void review(BaseApplication app) {
        System.out.println("Invalid action: Cannot review in " + getStateName() + " state.");
    }

    default void scheduleInterview(BaseApplication app) {
        System.out.println("Invalid action: Cannot schedule interview in " + getStateName() + " state.");
    }

    default void makeOffer(BaseApplication app) {
        System.out.println("Invalid action: Cannot make offer in " + getStateName() + " state.");
    }

    default void accept(BaseApplication app) {
        System.out.println("Invalid action: Cannot accept in " + getStateName() + " state.");
    }

    default void reject(BaseApplication app) {
        System.out.println("Invalid action: Cannot reject in " + getStateName() + " state.");
    }

    default void withdraw(BaseApplication app) {
        System.out.println("Invalid action: Cannot withdraw in " + getStateName() + " state.");
    }

    String getStateName();
    
}
