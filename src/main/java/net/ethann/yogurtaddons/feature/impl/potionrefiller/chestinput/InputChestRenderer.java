package net.ethann.yogurtaddons.feature.impl.potionrefiller.chestinput;

import net.ethann.yogurtaddons.util.RenderUtil;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Queue;

public class InputChestRenderer {
    private final Queue<ChestInput> queue;
    private final InputChestManager inputChestManager;
    private RenderType type;

    public InputChestRenderer( Queue<ChestInput> queue, InputChestManager inputChestManager) {
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

    private void renderAll() {
        if (!inputChestManager.isGroupSet()) return;
        for (ChestInput pos : inputChestManager.getAll()) {
            int r, g, b;

            switch (pos.orientation) {
                case TOP: {
                    r = 58;
                    g = 188;
                    b = 0;
                    break;
                }
                case BOTTOM: {
                    r = 255;
                    g = 240;
                    b = 60;
                    break;
                }
                case SIDE_BACK: {
                    r = 230;
                    g = 70;
                    b = 70;
                    break;
                }
                case SIDE_FRONT: {
                    r = 80;
                    g = 230;
                    b = 80;
                    break;
                }
                case SIDE_LEFT: {
                    r = 90;
                    g = 130;
                    b = 255;   // Light Blue (like water)
                    break;
                }
                case SIDE_RIGHT: {
                    r = 255;
                    g = 180;
                    b = 60;    // Gold-like Orange
                    break;
                }
                default: {
                    r = 255;
                    g = 255;
                    b = 255;
                    break;
                }
            }

            RenderUtil.drawBoxFaces(new AxisAlignedBB(pos.pos, pos.pos.add(1, 1, 1)), r, g, b, 100, true);
            RenderUtil.drawBoxOutline(new AxisAlignedBB(pos.pos, pos.pos.add(1, 1, 1)), r, g, b, 255, true);
        }
    }

    private void renderNext() {
        ChestInput pos = queue.peek();
        if (pos == null) return;
        RenderUtil.drawBoxFaces(new AxisAlignedBB(pos.pos, pos.pos.add(1, 1, 1)), 85, 255, 255, 100, true);
        RenderUtil.drawBoxOutline(new AxisAlignedBB(pos.pos, pos.pos.add(1, 1, 1)), 85, 255, 255, 255, true);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent e) {
        switch (type) {
            case ALL: {
                renderAll();
            }
            case NEXT: {
                renderNext();
            }
        }
    }

    public enum RenderType {
        NEXT,
        ALL
    }
}
