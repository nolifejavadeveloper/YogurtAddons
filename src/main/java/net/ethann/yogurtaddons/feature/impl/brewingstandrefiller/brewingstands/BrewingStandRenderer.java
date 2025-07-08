package net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.brewingstands;

import net.ethann.yogurtaddons.util.RenderUtil;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class BrewingStandRenderer {
    private final BrewingStandManager brewingStandManager;

    public BrewingStandRenderer(BrewingStandManager brewingStandManager) {
        this.brewingStandManager = brewingStandManager;
    }

    public void startRendering() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void stopRendering() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }


    private void renderAll() {
        for (Map.Entry<BlockPos, BrewingMaterial> brewingStand : brewingStandManager.getAll().entrySet()) {
            BlockPos pos = brewingStand.getKey();
            BrewingMaterial material = brewingStand.getValue();
            RenderUtil.drawBoxFaces(new AxisAlignedBB(pos, pos.add(1, 1, 1)), material.getR(), material.getG(), material.getB(), 100, true);
            RenderUtil.drawBoxOutline(new AxisAlignedBB(pos, pos.add(1, 1, 1)), material.getR(), material.getG(), material.getB(), 255, true);
        }
    }
    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        renderAll();
    }
}
