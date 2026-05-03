package com.careerforge.job;

public interface JobCollection {
    JobIterator createIterator();
    JobIterator createFilteredIterator(String filterType, String filterValue);
}
