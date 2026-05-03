package com.careerforge.resume;

public class Project {
    private String name;
    private String technologies;
    private String description;
    
    public Project(String name, String technologies, String description) {
        this.name = name;
        this.technologies = technologies;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getTechnologies() {
        return technologies;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " [" + technologies + "]: " + description;
    }
}
