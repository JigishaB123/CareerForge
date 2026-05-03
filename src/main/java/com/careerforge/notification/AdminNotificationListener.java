package com.careerforge.notification;

import com.careerforge.application.BaseApplication;
import com.careerforge.user.Admin;

/**
 * A concrete Observer that listens for system-wide events for an Admin.
 */
public class AdminNotificationListener implements EventListener {
    private Admin admin;

    public AdminNotificationListener(Admin admin) {
        this.admin = admin;
    }

    @Override
    public void update(String eventType, Object data) {
        System.out.println("\n--- ADMIN ALERT (" + admin.getFirstName() + ") ---");
        System.out.println("System Event: " + eventType);
        if (data instanceof BaseApplication)
            System.out.println("Details: Application " + ((BaseApplication) data).getApplicationId() + " was updated.");
    }
}