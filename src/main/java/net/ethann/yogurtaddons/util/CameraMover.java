package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class CameraMover {
    private float targetYaw;
    private float targetPitch;

    public CameraMover(float yaw, float pitch) {
        this.targetYaw = yaw;
        this.targetPitch = pitch;
    }

    public float nextYaw() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        float yaw = player.rotationYaw;
        if (targetYaw > yaw) {
            return yaw + 1;
        }else {
            return yaw - 1;
        }
    }

    public float nextPitch() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        float pitch = player.rotationPitch;
        if (targetPitch > pitch) {
            return pitch + 1;
        }else {
            return pitch - 1;
        }
    }

    public boolean done() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        return targetPitch == (int) (player.rotationPitch) && (int) player.rotationYaw == targetYaw;
    }

}