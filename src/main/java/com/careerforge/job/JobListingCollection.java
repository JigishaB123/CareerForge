package com.careerforge.job;

import java.util.ArrayList;
import java.util.List;

public class JobListingCollection implements JobCollection {

    private List<JobPosting> jobs;

    public JobListingCollection() {
        this.jobs = new ArrayList<>();
    }

    public void addJob(JobPosting job) {
        this.jobs.add(job);
    }

    @Override
    public JobIterator createIterator() {
        return new JobListingIterator(jobs, 10); // Default page size of 10
    }

    public JobIterator createFilteredIterator(
        String filterType,
        String filterValue
    ) {
        return new FilteredJobIterator(jobs, filterType, filterValue);
    }
}
