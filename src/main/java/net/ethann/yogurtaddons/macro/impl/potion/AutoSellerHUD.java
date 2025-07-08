package net.ethann.yogurtaddons.macro.impl.potion;

import net.ethann.yogurtaddons.util.Scheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

public class AutoSellerHUD {
    private final Scheduler scheduler;
    private static DecimalFormat df = new DecimalFormat("#.##");
    private long moneyCounter = 0;

    public AutoSellerHUD(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @SubscribeEvent
    public void onRenderText(RenderGameOverlayEvent.Text e) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        GlStateManager.pushMatrix();
        GlStateManager.scale(2, 2, 1);
        double time = scheduler.timeLeft() / 1000.0;
        fr.drawString("Next action: " + df.format(time < 0 ? 0 : time) + "s", 10, 20, 0xFFFFFF);
        GlStateManager.scale(1, 1, 1);
        fr.drawString("Coins earned: " + df.format(moneyCounter / 1000000.0) + "M", 10, 50, 0xFFFFAA00);
        GlStateManager.popMatrix();
    }

    public void addMoney(int i) {
        moneyCounter+= i;
    }

    public void resetMoney() {
        moneyCounter = 0;
    }

    public void enable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
