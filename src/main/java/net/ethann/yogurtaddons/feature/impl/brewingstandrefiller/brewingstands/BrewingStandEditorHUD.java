package net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.brewingstands;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BrewingStandEditorHUD {
    public void enable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent e) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        ScaledResolution scaledRes = new ScaledResolution(mc);
        String text = "Editing brewing stand";

        int centerX = scaledRes.getScaledWidth() / 2 - fr.getStringWidth(text) / 2;
        int centerY = scaledRes.getScaledHeight() / 2 + 5;

        fr.drawString(text, centerX, centerY + 20, 0xFFFFFFFF);

    }
}
