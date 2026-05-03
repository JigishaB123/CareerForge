package com.careerforge.notification;

/**
 * The Observer interface. All concrete listeners will implement this interface
 * to receive notifications from the EventManager.
 */
public interface EventListener {
    void update(String eventType, Object data);
}