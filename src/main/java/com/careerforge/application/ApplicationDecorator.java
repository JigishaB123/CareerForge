package com.careerforge.application;

public abstract class ApplicationDecorator implements ApplicationComponent {


    protected ApplicationComponent wrappedComponent;

    public ApplicationDecorator(ApplicationComponent wrappedComponent) {
        this.wrappedComponent = wrappedComponent;
    }

    @Override
    public String getDescription() {
        return wrappedComponent.getDescription();
    }

    @Override
    public int getStrength() {
        return wrappedComponent.getStrength();
    }

    public ApplicationComponent getWrappedComponent() {
        return wrappedComponent;
    }
}
