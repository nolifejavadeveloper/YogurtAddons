package net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.brewingstands;

import lombok.Setter;
import net.ethann.yogurtaddons.util.ChatUtil;
import net.ethann.yogurtaddons.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BrewingStandEditor {
    private final BrewingStandManager inputChestManager;
    private final BrewingStandRenderer inputChestRenderer;
    private final BrewingStandEditorHUD hud;
    @Setter
    private BrewingMaterial mode = BrewingMaterial.GLOWSTONE_BLOCK;
    private boolean enabled;
    public BrewingStandEditor(BrewingStandManager inputChestManager, BrewingStandRenderer inputChestRenderer) {
        this.inputChestManager = inputChestManager;
        this.inputChestRenderer = inputChestRenderer;
        this.hud = new BrewingStandEditorHUD();
    }

    private void enable() {
        ChatUtil.log("enabling brewing stand editor...");

        inputChestRenderer.startRendering();
        hud.enable();
        MinecraftForge.EVENT_BUS.register(this);

        enabled = true;
    }

    private void disable() {
        ChatUtil.log("disabling brewing stand editor...");

        inputChestRenderer.stopRendering();
        hud.disable();
        MinecraftForge.EVENT_BUS.unregister(this);

        enabled = false;
        inputChestManager.save();
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

            BlockPos p = getBrewingStand();
            if (p == null) return;

            if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && !mc.thePlayer.isSneaking()) {
                return;
            }

            e.setCanceled(true);

            inputChestManager.setBrewingStand(p, mode);
        }
    }

    private BlockPos getBrewingStand() {
        BlockPos pos = PlayerUtil.getBlockLookingAt();
        if (pos == null) return null;

        Block b = Minecraft.getMinecraft().thePlayer.getEntityWorld().getBlockState(pos).getBlock();
        if (b == Blocks.brewing_stand) {
            return pos;
        }

        return null;
    }
}
