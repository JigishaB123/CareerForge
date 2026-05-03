package com.careerforge.job;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import com.careerforge.job.JobPosting;

public class FilteredJobIterator implements JobIterator {

    private List<JobPosting> filteredJobs;
    private int position;
    private String filterType;
    private String filterValue;

    public FilteredJobIterator(
        List<JobPosting> jobs,
        String filterType,
        String filterValue
    ) {
        this.filterType = filterType;
        this.filterValue = filterValue;
        this.position = 0;
        this.filteredJobs = applyFilter(jobs);
    }

    @Override
    public boolean hasNext() {
        return position < filteredJobs.size();
    }

    @Override
    public JobPosting next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more matching job postings.");
        }
        return filteredJobs.get(position++);
    }

    @Override
    public void reset() {
        this.position = 0;
    }

    public List<JobPosting> applyFilter(List<JobPosting> jobs) {
        List<JobPosting> result = new ArrayList<>();
        for (JobPosting job : jobs) {
            if ("type".equalsIgnoreCase(filterType) &&
                job.getType().equalsIgnoreCase(filterValue)
            ) {
                result.add(job);
            } else if ("location".equalsIgnoreCase(filterType) &&
                job.getLocation().equalsIgnoreCase(filterValue)
            ) {
                result.add(job);
            } else if ("skill".equalsIgnoreCase(filterType)) {
                for (String skill : job.getRequiredSkills()) {
                    if (skill.equalsIgnoreCase(filterValue)) {
                        result.add(job);
                        break;
                    }
                }
            }
        }
        return result;
    }
}
