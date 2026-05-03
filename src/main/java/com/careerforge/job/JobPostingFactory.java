package com.careerforge.job;

import java.util.Map;
public abstract class JobPostingFactory {
    public abstract JobPosting createPosting(Map<String, String> details);
}
