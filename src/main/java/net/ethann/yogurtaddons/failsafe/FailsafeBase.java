package net.ethann.yogurtaddons.failsafe;

import net.minecraftforge.common.MinecraftForge;

public abstract class FailsafeBase {
    private boolean enabled = false;
    public FailsafeBase() {

    }

    public void enable() {
        enabled = true;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void disable() {
        enabled = false;
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
