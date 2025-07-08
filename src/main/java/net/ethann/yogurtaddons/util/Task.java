package net.ethann.yogurtaddons.util;

import net.ethann.yogurtaddons.YogurtAddons;
import net.ethann.yogurtaddons.notification.Notification;
import net.ethann.yogurtaddons.notification.NotificationLevel;
import net.ethann.yogurtaddons.notification.NotificationService;
import net.ethann.yogurtaddons.task.TaskManager;

public abstract class Task {

    public void enable() {
        if (shouldRegisterTask()) {
            TaskManager.getInstance().addTask(this);
        }
        ChatUtil.log("enabling " + getName() + "...");
        NotificationService.getInstance().send(new Notification().withTitle("Task Enabled").withDetails("Starting: **" + this.getName() + "**").withLevel(NotificationLevel.INFO), NotificationService.DISCORD, NotificationService.DESKTOP);
        onEnable();
    }

    public void disable() {
        if (shouldRegisterTask()) {
            TaskManager.getInstance().removeTask(this);
        }
        ChatUtil.log("disabling " + getName() + "...");
        NotificationService.getInstance().send(new Notification().withTitle("Task Disabled").withDetails("Stopping: **" + this.getName() + "**").withLevel(NotificationLevel.WARNING), NotificationService.DISCORD, NotificationService.DESKTOP);
        onDisable();
    }

    protected boolean shouldRegisterTask() {
        return false;
    }

    protected abstract void onEnable();
    protected abstract void onDisable();
    protected abstract String getName();
}
