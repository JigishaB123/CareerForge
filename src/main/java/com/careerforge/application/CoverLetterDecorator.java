package com.careerforge.application;

public class CoverLetterDecorator extends ApplicationDecorator {

    private String coverLetterText;

    public CoverLetterDecorator(ApplicationComponent wrappedComponent, String coverLetterText) {
        super(wrappedComponent);
        this.coverLetterText = coverLetterText;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Cover Letter";
    }

    @Override
    public int getStrength() {
        return super.getStrength() + 10;
    }

    public String getCoverLetterText() {
        return coverLetterText;
    }


    
}
