package com.careerforge.application;

public class ReferralDecorator extends ApplicationDecorator {

    private String referrerName;
    private String referrerEmail;
    private String referrerTitle;

    public ReferralDecorator(ApplicationComponent wrappedComponent, String referrerName, 
                              String referrerEmail, String referrerTitle) {
        super(wrappedComponent);
        this.referrerName = referrerName;
        this.referrerEmail = referrerEmail;
        this.referrerTitle = referrerTitle;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Referral from " + referrerName + " (" + referrerTitle + ")";
    }

    @Override
    public int getStrength() {
        return super.getStrength() + 20;
    }

    public String getReferrerName() {
        return referrerName;
    }

    public String getReferrerEmail() {
        return referrerEmail;
    }

    public String getReferrerTitle() {
        return referrerTitle;
    }


    
}
