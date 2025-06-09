package net.ethann.yogurtaddons.macro;

import net.minecraftforge.common.MinecraftForge;

public abstract class MacroBase {
    private boolean enabled = false;
    public MacroBase() {

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
