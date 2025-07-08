package net.ethann.yogurtaddons.notification;

import lombok.Getter;
import net.ethann.yogurtaddons.notification.impl.desktop.DesktopNotificationService;
import net.ethann.yogurtaddons.notification.impl.discord.DiscordNotificationService;
import org.reflections.Reflections;

import java.util.HashMap;

public class NotificationService {
    @Getter
    private static final NotificationService instance = new NotificationService();

    public static Class<? extends Notifier> DISCORD = DiscordNotificationService.class;
    public static Class<? extends Notifier> DESKTOP = DesktopNotificationService.class;

    private final HashMap<Class<? extends Notifier>, Notifier> services = new HashMap<>();

    public NotificationService() {
        Reflections refl = new Reflections("net.ethann.yogurtaddons.notification.impl");
        for (Class<? extends Notifier> clazz : refl.getSubTypesOf(Notifier.class)) {
            try {
                System.out.println("found: " + clazz.getName());
                services.put(clazz, clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SafeVarargs
    public final void send(Notification notification, Class<? extends Notifier>... classes) {
        for (Class<? extends Notifier> clazz : classes) {
            Notifier notifier = services.get(clazz);
            if (notifier != null) notifier.notify(notification);
        }
    }
}
