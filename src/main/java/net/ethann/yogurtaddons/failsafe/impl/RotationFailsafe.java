package net.ethann.yogurtaddons.failsafe.impl;

import net.ethann.yogurtaddons.failsafe.FailsafeBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RotationFailsafe extends FailsafeBase {
    float yaw = 0;
    float pitch = 0;

    @Override
    protected String getName() {
        return "rotation";
    }

    protected void onEnable() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        yaw = player.rotationYaw;
        pitch = player.rotationPitch;
    }

    protected void onDisable() {

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if (player.rotationYaw != yaw) {
            trigger();
        }

        if (player.rotationPitch != pitch) {
            trigger();
        }
    }
}
