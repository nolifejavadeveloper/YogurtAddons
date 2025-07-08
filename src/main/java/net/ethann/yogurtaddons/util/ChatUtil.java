package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final String PREFIX = "§a§lYo§e§lgurt§r§f";

    public static void sendCommand(String s) {
        if (mc.thePlayer != null) {
            mc.thePlayer.sendChatMessage("/" + s);
        }
    }

    public static void log(String s) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(PREFIX + " > " + s));
        }
    }

    public static void warn(String s) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(PREFIX + " §6[WARN]§r > " + s));
        }
    }

    public static void error(String s) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatComponentMessage(new ChatComponentText(PREFIX + " §c[Error]§r > " + s));
        }
    }
}
