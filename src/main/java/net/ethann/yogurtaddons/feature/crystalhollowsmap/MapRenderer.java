package net.ethann.yogurtaddons.feature.crystalhollowsmap;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MapRenderer {
    private final CrystalHollowsMap map;

    private final static int chunkRenderSize = 5;
    public MapRenderer(final CrystalHollowsMap map) {
        this.map = map;
    }

    void enable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent.Post event) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(100, 100, 0);
        for (int x = 0; x < CrystalHollows.LENGTH_X; x++) {
            for (int y = 0; y < CrystalHollows.LENGTH_Y; y++) {
                CrystalHollowsChunk chunk = map.getCrystalHollows().getChunkByIndex(x, y);
                if (chunk == null) {
                    Gui.drawRect(chunkRenderSize * x, chunkRenderSize * y, chunkRenderSize + chunkRenderSize * x, chunkRenderSize  * y - chunkRenderSize, 0xFFFFFFFF);
                }else {
                    Gui.drawRect(chunkRenderSize * x, chunkRenderSize * y, chunkRenderSize + chunkRenderSize * x, chunkRenderSize  * y - chunkRenderSize, 0xAAFF0000);
                }
            }
        }
        GlStateManager.popMatrix();
    }

}
