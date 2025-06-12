package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class ControlUtil {
    public static void rightClick() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        player.interactFirst(player);
    }


}
