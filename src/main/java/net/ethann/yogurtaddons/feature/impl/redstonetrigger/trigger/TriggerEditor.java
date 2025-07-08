package net.ethann.yogurtaddons.feature.impl.redstonetrigger.trigger;

import lombok.Setter;
import net.ethann.yogurtaddons.util.ChatUtil;
import net.ethann.yogurtaddons.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TriggerEditor {
    private final TriggerManager triggerManager;
    private final TriggerRenderer renderer;
    @Setter
    private TriggerType mode = TriggerType.ON;
    @Setter
    private String nextName = "unnamed";

    private boolean enabled;

    public TriggerEditor(TriggerManager triggerManager, TriggerRenderer renderer) {
        this.triggerManager = triggerManager;
        this.renderer = renderer;
    }

    private void enable() {
        ChatUtil.log("enabling chest input editor...");

        renderer.setShowAll(true);

        MinecraftForge.EVENT_BUS.register(this);

        enabled = true;
    }

    private void disable() {
        ChatUtil.log("disabling chest input editor...");

        renderer.setShowAll(false);
        MinecraftForge.EVENT_BUS.unregister(this);

        enabled = false;
        triggerManager.save();
    }

    public void toggle() {
        if (enabled) {
            disable();
        }else {
            enable();
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent e) {

    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent e) {
        if (!e.entityPlayer.equals(Minecraft.getMinecraft().thePlayer)) return;
        if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.thePlayer.getHeldItem() != null) return;

            BlockPos p = getRedstoneDustLookingAt();

            if (p == null) {
                ChatUtil.warn("wrong block");
                return;
            }

            triggerManager.toggle(mode, p, nextName);
        }
    }

    private BlockPos getRedstoneDustLookingAt() {
        BlockPos pos = PlayerUtil.getBlockLookingAt();
        if (pos == null) return null;

        Block b = Minecraft.getMinecraft().thePlayer.getEntityWorld().getBlockState(pos).getBlock();
        if (b == Blocks.redstone_wire) {
            return pos;
        }

        return null;
    }
}
