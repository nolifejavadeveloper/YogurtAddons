package net.ethann.yogurtaddons.notification.impl.desktop;

import net.ethann.yogurtaddons.notification.Notification;
import net.ethann.yogurtaddons.notification.NotificationLevel;
import net.ethann.yogurtaddons.notification.Notifier;

import java.awt.*;

public class DesktopNotificationService implements Notifier {
    private TrayIcon icon;
    public DesktopNotificationService() {
        System.out.println("registering desktop notification service");
        if (!SystemTray.isSupported()) {
            System.out.println("Tray not supported");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().createImage(""), "");
        icon.setImageAutoSize(true);
        try {
            tray.add(icon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        this.icon = icon;

        notify(Notification.create().withTitle("test").withDetails("make sure you can see this message!").withLevel(NotificationLevel.WARNING));
    }
    @Override
    public void notify(Notification notification) {
        icon.displayMessage(notification.getTitle(), notification.getDetails(), TrayIcon.MessageType.WARNING);
    }
}
