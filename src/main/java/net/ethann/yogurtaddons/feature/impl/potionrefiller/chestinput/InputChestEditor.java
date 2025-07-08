package net.ethann.yogurtaddons.feature.impl.potionrefiller.chestinput;

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

public class InputChestEditor {
    private final InputChestManager inputChestManager;
    private final InputChestRenderer inputChestRenderer;
    private final InputChestEditorHUD hud;
    @Setter
    private ChestPosition mode = ChestPosition.BOTTOM;
    private String currentGroup = "";

    private boolean enabled;
    public InputChestEditor(InputChestManager inputChestManager, InputChestRenderer inputChestRenderer) {
        this.inputChestManager = inputChestManager;
        this.inputChestRenderer = inputChestRenderer;
        this.hud = new InputChestEditorHUD();
    }

    private void enable() {
        ChatUtil.log("enabling chest input editor...");

        inputChestRenderer.setRenderingType(InputChestRenderer.RenderType.ALL);
        inputChestRenderer.startRendering();
        hud.enable();
        MinecraftForge.EVENT_BUS.register(this);

        enabled = true;
    }

    private void disable() {
        ChatUtil.log("disabling chest input editor...");

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

            BlockPos p = getChestLookingAt();
            if (p == null) return;

            if (e.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && !mc.thePlayer.isSneaking()) {
                return;
            }

            e.setCanceled(true);

            if (!inputChestManager.isGroupSet()) {
                ChatUtil.warn("please select a group first");
                return;
            }

            inputChestManager.toggleChest(new ChestInput(p, mode));
        }
    }

    private BlockPos getChestLookingAt() {
        BlockPos pos = PlayerUtil.getBlockLookingAt();
        if (pos == null) return null;

        Block b = Minecraft.getMinecraft().thePlayer.getEntityWorld().getBlockState(pos).getBlock();
        if (b == Blocks.chest || b == Blocks.trapped_chest) {
            return pos;
        }

        return null;
    }
}
