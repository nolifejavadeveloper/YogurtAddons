package net.ethann.yogurtaddons.failsafe;

import lombok.Getter;
import net.ethann.yogurtaddons.macro.MacroBase;
import net.ethann.yogurtaddons.notification.Notification;
import net.ethann.yogurtaddons.notification.NotificationLevel;
import net.ethann.yogurtaddons.notification.NotificationService;
import net.ethann.yogurtaddons.task.TaskManager;
import net.ethann.yogurtaddons.util.ChatUtil;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;

public class FailsafeManager {
    private boolean enabled = false;
    @Getter
    private static final FailsafeManager instance = new FailsafeManager();
    private final HashMap<Class<? extends FailsafeBase>, FailsafeBase> failsafes = new HashMap<>();
    public FailsafeManager() {
        Reflections reflections = new Reflections("net.ethann.yogurtaddons.failsafe.impl");
        Set<Class<? extends FailsafeBase>> classes = reflections.getSubTypesOf(FailsafeBase.class);

        for (Class<? extends FailsafeBase> clazz : classes) {
            try {
                FailsafeBase base = clazz.newInstance();
                failsafes.put(clazz, base);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("failed to initialize failsafe: " + clazz.getName());
            }
        }
    }

    public void enable() {
        failsafes.forEach((key, value) -> value.enable());
        enabled = true;
    }

    public void disable() {
        failsafes.forEach((key, value) -> value.disable());
    }

    public void triggerFailsafe(FailsafeBase failsafe) {
        NotificationService.getInstance().send(new Notification().withLevel(NotificationLevel.SEVERE).withTitle("Potential Emergency Detected").withDetails("The following failsafe has been triggered: " + failsafe.getName()), NotificationService.DESKTOP, NotificationService.DISCORD);
        TaskManager.getInstance().stopAll();
        ChatUtil.log("failsafe activated, all tasks has been disabled.");
    }
}
