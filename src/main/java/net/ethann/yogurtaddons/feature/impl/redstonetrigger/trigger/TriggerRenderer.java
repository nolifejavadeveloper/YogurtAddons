package net.ethann.yogurtaddons.feature.impl.redstonetrigger.trigger;

import lombok.Setter;
import net.ethann.yogurtaddons.util.RenderUtil;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerRenderer {
    private final TriggerManager triggerManager;

    private long lastSwitch = 200;
    private boolean isShowing = false;
    @Setter
    private boolean showAll = false;

    public TriggerRenderer(TriggerManager triggerManager) {
        this.triggerManager = triggerManager;
    }

    public void enable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent e) {
        for (Trigger t : triggerManager.getAllTriggers()) {
            int r,g,b;
            r = 255;
            g = 85;
            b = 85;
            if (t.getType() == TriggerType.ON) {
                r = 85;
                g = 255;
                b = 85;
            }
            if (showAll) {
                RenderUtil.drawBoxFaces(new AxisAlignedBB(t.getBlockPos(), t.getBlockPos().add(1, 1, 1)), r, g, b, 100, true);
                RenderUtil.drawBoxOutline(new AxisAlignedBB(t.getBlockPos(), t.getBlockPos().add(1, 1, 1)), r, g, b, 255, true);
            }else {
                if (!t.isReady()) {
                    if (isShowing) {
                        RenderUtil.drawBoxFaces(new AxisAlignedBB(t.getBlockPos(), t.getBlockPos().add(1, 1, 1)), r, g, b, 100, true);
                        RenderUtil.drawBoxOutline(new AxisAlignedBB(t.getBlockPos(), t.getBlockPos().add(1, 1, 1)), r, g, b, 255, true);
                    }
                }

                if (System.currentTimeMillis() - lastSwitch > 1000) {
                    isShowing = !isShowing;
                    lastSwitch = System.currentTimeMillis();
                }
            }
        }
    }
}
