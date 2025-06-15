package net.ethann.yogurtaddons.macro.impl.potion.chestinput;

import net.ethann.yogurtaddons.util.RenderUtil;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Queue;

public class InputChestRenderer {
    private final Queue<BlockPos> queue;
    private final InputChestManager inputChestManager;
    private RenderType type;

    public InputChestRenderer( Queue<BlockPos> queue, InputChestManager inputChestManager) {
        this.queue = queue;
        this.inputChestManager = inputChestManager;
    }

    public void startRendering() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void stopRendering() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void setRenderingType(InputChestRenderer.RenderType type) {
        this.type = type;
    }

    public void renderAll() {
        for (BlockPos pos : inputChestManager.getAll()) {
            RenderUtil.drawBoxFaces(new AxisAlignedBB(pos, pos.add(1, 1, 1)), 85, 255, 255, 100, true);
            RenderUtil.drawBoxOutline(new AxisAlignedBB(pos, pos.add(1, 1, 1)), 85, 255, 255, 255, true);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        switch (type) {
            case ALL: {
                renderAll();
            }
            case NEXT: {
                // TODO: implement me
            }
        }
    }

    public enum RenderType {
        NEXT,
        ALL
    }
}
