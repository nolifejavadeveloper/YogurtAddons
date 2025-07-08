package net.ethann.yogurtaddons.failsafe.impl;

import net.ethann.yogurtaddons.failsafe.FailsafeBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WorldChangeFailsafe extends FailsafeBase {
    private World startWorld = null;
    @Override
    protected String getName() {
        return "world";
    }

    protected void onEnable() {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        startWorld = player.worldObj;
    }

    protected void onDisable() {

    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if (player.worldObj != null) {
            if (startWorld != player.worldObj) {
                trigger();
            }
        }
    }
}
