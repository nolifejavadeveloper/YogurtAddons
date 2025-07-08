package net.ethann.yogurtaddons.failsafe.impl;

import net.ethann.yogurtaddons.failsafe.FailsafeBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PositionFailsafe extends FailsafeBase {
    double x = 0;
    double y = 0;
    double z = 0;

    @Override
    protected String getName() {
        return "position";
    }

    protected void onEnable() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        x = player.posX;
        y = player.posY;
        z = player.posZ;
    }

    protected void onDisable() {

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if (player.posX != x) {
            trigger();
        }

        if (player.posY != y) {
            trigger();
        }

        if (player.posZ != z) {
            trigger();
        }
    }
}
