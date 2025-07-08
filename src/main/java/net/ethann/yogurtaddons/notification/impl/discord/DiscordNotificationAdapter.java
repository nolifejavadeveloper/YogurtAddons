package net.ethann.yogurtaddons.notification.impl.discord;

import net.ethann.yogurtaddons.notification.Notification;
import net.ethann.yogurtaddons.notification.NotificationLevel;
import net.minecraft.client.Minecraft;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class DiscordNotificationAdapter {
    private static final HashMap<NotificationLevel, Integer> levelToColorMap = new HashMap<>();

    static {
        levelToColorMap.put(NotificationLevel.INFO, 7183154);
        levelToColorMap.put(NotificationLevel.WARNING, 16763955);
        levelToColorMap.put(NotificationLevel.SEVERE, 16734006);
        levelToColorMap.put(NotificationLevel.FATAL, 13816591);
    }

    public static String adapt(Notification notif) {
        String format = "{\n" +
                "  \"embeds\": [\n" +
                "    {\n" +
                "        \"title\": \"%s\",\n" +
                "        \"description\": \"%s\",\n" +
                "        \"color\": %d,\n" +
                "        \"timestamp\": \"%s\",\n" +
                "        \"footer\": {\n" +
                "        \"text\": \"Yogurt Notification Service\"\n" +
                "        },\n" +
                "        \"author\": {\n" +
                "        \"name\": \"%s\"\n" +
                "    }\n" +
                "  }\n" +
                "  ]\n" +
                "}";
        String time = DateTimeFormatter.ISO_INSTANT.format(Instant.now());

        String name = "-";
        if (Minecraft.getMinecraft().thePlayer != null) {
            name = Minecraft.getMinecraft().thePlayer.getName();
        }

        return String.format(format, notif.getTitle(), notif.getDetails(), levelToColorMap.getOrDefault(notif.getLevel(), 0), time, name);
    }
}


