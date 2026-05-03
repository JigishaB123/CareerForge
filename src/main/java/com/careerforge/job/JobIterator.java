package com.careerforge.job;

import com.careerforge.job.JobPosting;

public interface JobIterator {
    boolean hasNext();
    JobPosting next();
    void reset();
}
