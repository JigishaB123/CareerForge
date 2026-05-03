package com.careerforge.application;

public class PortfolioDecorator extends ApplicationDecorator {

    private String portfolioUrl;

    public PortfolioDecorator(ApplicationComponent wrappedComponent, String portfolioUrl) {
        super(wrappedComponent);
        this.portfolioUrl = portfolioUrl;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Portfolio (" + portfolioUrl + ")";
    }

    @Override
    public int getStrength() {
        return super.getStrength() + 15;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }
    
}
