package com.careerforge.notification;

import com.careerforge.application.BaseApplication;
import com.careerforge.user.Employer;
import com.careerforge.user.Student;
/**
 * A concrete Observer that listens for events relevant to an Employer.
 */
public class EmployerNotificationListener implements EventListener {
    private Employer employer;

    public EmployerNotificationListener(Employer employer) {
        this.employer = employer;
    }

    @Override
    public void update(String eventType, Object data) {
        if (data instanceof BaseApplication && ((BaseApplication) data).getJob().getCompany().equals(employer.getCompanyName())) {
            BaseApplication app = (BaseApplication) data;
            Student student = app.getStudent();
            if ("APPLICATION_SUBMITTED".equals(eventType)) {
                String message = student.getFirstName() + " " + student.getLastName()
                    + " submitted an application for " + app.getJob().getJobId()
                    + " '" + app.getJob().getTitle() + "'.";
                employer.addNotification(message);
            } else if ("APPLICATION_STATUS_CHANGED".equals(eventType)
                    && "Withdrawn".equals(app.getCurrentState().getStateName())) {
                String message = student.getFirstName() + " " + student.getLastName()
                    + " application for '" + app.getJob().getTitle()
                    + "' was withdrawn.";
                employer.addNotification(message);
            }
        }
    }
}
