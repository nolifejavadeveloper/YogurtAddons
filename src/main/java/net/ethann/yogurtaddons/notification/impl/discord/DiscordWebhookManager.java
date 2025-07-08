package net.ethann.yogurtaddons.notification.impl.discord;

import lombok.Getter;
import net.ethann.yogurtaddons.util.FileUtil;
import net.minecraftforge.client.ClientCommandHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

public class DiscordWebhookManager {
    public DiscordWebhookManager() {
        load();

        ClientCommandHandler.instance.registerCommand(new DiscordWebhookCommand(this));
    }

    private static final String FILE_NAME = "webhook.txt";
    @Getter
    private String serviceUrl = "";
    @Getter
    private String webhookUrl = "";

    private void load() {
        File file = FileUtil.getConfigFileFromModPath(FILE_NAME);
        try {
            String s = FileUtil.readFile(file);
            if (s.equals("")) {
                serviceUrl = "";
                webhookUrl = "";
                return;
            }
            serviceUrl = s.split(",")[0];
            webhookUrl = s.split(",")[1];
        } catch (NoSuchFileException e)  {
            System.out.println(FILE_NAME + " not found");
            return;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read " + FILE_NAME + ": " + e);
        }
    }

    private void save() {
        File file = FileUtil.getConfigFileFromModPath(FILE_NAME);

        try {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileUtil.writeFile(file, serviceUrl + "," + webhookUrl);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write " + FILE_NAME + ": " + e);
        }
    }

    public void setWebhookUrl(String url) {
        webhookUrl = url;
        save();
    }

    public void setServiceUrl(String url) {
        serviceUrl = url;
        save();
    }
}
