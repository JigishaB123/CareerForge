package com.careerforge.notification;

import com.careerforge.application.BaseApplication;
import com.careerforge.job.JobPosting;
import com.careerforge.user.Student;
/**
 * A concrete Observer that listens for events relevant to a Student.
 */
public class StudentNotificationListener implements EventListener {
    private Student student;

    public StudentNotificationListener(Student student) {
        this.student = student;
    }

    @Override
    public void update(String eventType, Object data) {
        if (eventType.equals("APPLICATION_STATUS_CHANGED") && data instanceof BaseApplication
                && ((BaseApplication) data).getStudent().getUserId().equals(student.getUserId())) {
            BaseApplication app = (BaseApplication) data;
            if ("Withdrawn".equals(app.getCurrentState().getStateName())) {
                return;
            }
            student.addNotification("Application for '" + app.getJob().getTitle()
                + "' at " + app.getJob().getCompany()
                + " moved to: " + app.getCurrentState().getStateName());
        } else if (eventType.equals("JOB_POSTED") && data instanceof JobPosting) {
            JobPosting job = (JobPosting) data;
            student.addNotification("New job posted: '" + job.getTitle()
                + "' at " + job.getCompany()
                + " (" + job.getType() + ") — " + job.getLocation());
        }
    }
}
