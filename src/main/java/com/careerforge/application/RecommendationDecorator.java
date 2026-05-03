package com.careerforge.application;

public class RecommendationDecorator extends ApplicationDecorator {

    private String recommenderName;
    private String letterContent;

    public RecommendationDecorator(ApplicationComponent wrappedComponent, String recommenderName, 
                                    String letterContent) {
        super(wrappedComponent);
        this.recommenderName = recommenderName;
        this.letterContent = letterContent;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Recommendation from " + recommenderName;
    }

    @Override
    public int getStrength() {
        return super.getStrength() + 15;
    }

    public String getRecommenderName() {
        return recommenderName;
    }

    public String getLetterContent() {
        return letterContent;
    }
    
}
