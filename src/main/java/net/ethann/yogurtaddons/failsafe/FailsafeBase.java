package net.ethann.yogurtaddons.failsafe;

import net.ethann.yogurtaddons.util.ChatUtil;
import net.minecraftforge.common.MinecraftForge;

public abstract class FailsafeBase {
    private boolean enabled = false;
    private long started;

    protected abstract String getName();

    public void enable() {
        started = System.currentTimeMillis();
        enabled = true;
        MinecraftForge.EVENT_BUS.register(this);

        onDisable();
    }

    public void disable() {
        enabled = false;
        MinecraftForge.EVENT_BUS.unregister(this);

        onEnable();
    }

    protected void trigger() {
        if (System.currentTimeMillis() - started < 1000) return; // 1 second delay to starting failsafe
        ChatUtil.warn("Triggering failsafe: " + getName());
        FailsafeManager.getInstance().triggerFailsafe(this);
    }

    protected abstract void onEnable();
    protected abstract void onDisable();
}
