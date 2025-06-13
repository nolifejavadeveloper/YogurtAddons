package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;

public class ChatUtil {
    public static void sendCommand(String s) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + s);
    }
}
