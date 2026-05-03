package com.careerforge.job;

import java.util.List;
import java.util.NoSuchElementException;
import com.careerforge.job.JobPosting;

public class JobListingIterator implements JobIterator {

    private List<JobPosting> jobs;
    private int position;
    private int pageSize;

    public JobListingIterator(List<JobPosting> jobs, int pageSize) {
        this.jobs = jobs;
        this.position = 0;
        this.pageSize = pageSize;
    }

    @Override
    public boolean hasNext() {
        return position < jobs.size();
    }

    @Override
    public JobPosting next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more job postings available.");
        }
        return jobs.get(position++);
    }

    @Override
    public void reset() {
        this.position = 0;
    }

    public int getCurrentPage() {
        if (pageSize == 0) return 1;
        return (position / pageSize) + 1;
    }
}
