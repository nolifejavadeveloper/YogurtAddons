package net.ethann.yogurtaddons.macro.impl.potion;

import net.ethann.yogurtaddons.util.Scheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

public class AutoSellChestHUD {
    private final Scheduler scheduler;
    private static DecimalFormat df = new DecimalFormat("#.##");

    public AutoSellChestHUD(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @SubscribeEvent
    public void onRenderText(RenderGameOverlayEvent.Text e) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        GlStateManager.pushMatrix();
        GlStateManager.scale(2, 2, 1);
        double time = scheduler.timeLeft() / 1000.0;
        fr.drawString(df.format(time < 0 ? 0 : time), 10, 20, 0xFFFFFF);
        GlStateManager.popMatrix();
    }

    public void enable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
