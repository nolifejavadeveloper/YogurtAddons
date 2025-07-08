package net.ethann.yogurtaddons.notification.impl.discord;

import com.google.gson.Gson;
import net.ethann.yogurtaddons.notification.Notification;
import net.ethann.yogurtaddons.notification.Notifier;
import net.ethann.yogurtaddons.util.ChatUtil;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordNotificationService implements Notifier {
    private final DiscordWebhookManager discordWebhookManager;
    public DiscordNotificationService() {
        System.out.println("registering discord notification service");
        this.discordWebhookManager = new DiscordWebhookManager();
    }
    @Override
    public void notify(Notification notification) {
        new Thread(() -> {
            try {
                String serviceUrl = discordWebhookManager.getServiceUrl();
                String webhookUrl = discordWebhookManager.getWebhookUrl();
                if (serviceUrl.isEmpty()) {
                    ChatUtil.warn("invalid url for webhook");
                    return;
                }
                String jsonPayload = new Gson().toJson(new DiscordServiceRequest(webhookUrl, DiscordNotificationAdapter.adapt(notification)));

                URL url = new URL(serviceUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonPayload.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int code = conn.getResponseCode();
                ChatUtil.log("webhook response: " + code);

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                ChatUtil.error("unable to post to webhook: " + e.getMessage());
            }
        }).start();
    }


}
