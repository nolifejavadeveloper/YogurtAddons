package net.ethann.yogurtaddons.feature.common;

import net.ethann.yogurtaddons.util.RenderUtil;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class UsedBlockRenderer {
    private final List<BlockPos> blocks;

    public UsedBlockRenderer(List<BlockPos> blocks) {
        this.blocks = blocks;
    }

    public void enable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent e) {
        for (BlockPos pos : blocks) {
            RenderUtil.drawBoxFaces(new AxisAlignedBB(pos, pos.add(1, 1, 1)), 85, 255, 85, 100, true);
            RenderUtil.drawBoxOutline(new AxisAlignedBB(pos, pos.add(1, 1, 1)), 85, 255, 85, 255, true);
        }
    }
}
