package net.ethann.yogurtaddons.notification;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Notification {
    private String title = "";
    private String details = "";
    private NotificationLevel level = NotificationLevel.INFO;
    public static Notification create() {
        return new Notification();
    }

    public Notification withTitle(String s) {
        this.title = s;
        return this;
    }

    public Notification withDetails(String s) {
        this.details = s;
        return this;
    }

    public Notification withLevel(NotificationLevel l) {
        this.level = l;
        return this;
    }

}
