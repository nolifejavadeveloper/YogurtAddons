package net.ethann.yogurtaddons.feature.impl.brewingstandrefresher;

import net.ethann.yogurtaddons.feature.common.UsedBlockRenderer;
import net.ethann.yogurtaddons.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class BrewingStandRefresher extends Task {
    private static final int BREWING_STAND_REFRESH_SLOT = 38;

    private final List<BlockPos> finishedStands;
    private final UsedBlockRenderer renderer;
    private final Scheduler scheduler;

    private long lastClick;

    private RefreshStage stage;

    private boolean enabled = false;

    public BrewingStandRefresher() {
        this.scheduler = new Scheduler();
        this.finishedStands = new ArrayList<>();
        this.renderer = new UsedBlockRenderer(finishedStands);

        lastClick = System.currentTimeMillis();

        ClientCommandHandler.instance.registerCommand(new BrewingStandRefresherCommand(this));
    }

    public void onEnable() {
        enabled = true;
        finishedStands.clear();
        stage = RefreshStage.IDLE;
        MinecraftForge.EVENT_BUS.register(this);
        renderer.enable();
    }

    public void onDisable() {
        enabled = false;
        MinecraftForge.EVENT_BUS.unregister(this);
        renderer.disable();
    }

    @Override
    protected String getName() {
        return "Brewing Stand Refresher";
    }

    private void start() {
        scheduler.schedule(DelayUtil.getDelay(700, 250));
        stage = RefreshStage.TAKE_ITEM;
        ChatUtil.log("cycle started");
    }

    public void toggle() {
        if (enabled) {
            disable();
        }else {
            enable();
        }
    }

    private void finish() {
        stage = RefreshStage.IDLE;
        ChatUtil.log("cycle finished");
    }

    @SubscribeEvent
    public void onBrewingStandOpen(PlayerInteractEvent event) {
        if (!event.entityPlayer.equals(Minecraft.getMinecraft().thePlayer)) return;
        if ((lastClick + 60) > System.currentTimeMillis()) return;
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            if (Minecraft.getMinecraft().thePlayer.getEntityWorld().getBlockState(event.pos).getBlock() == Blocks.brewing_stand) {
                ChatUtil.log("detected brewing stand open");

                finishedStands.add(event.pos);

                lastClick = System.currentTimeMillis();
                start();
                return;
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (!scheduler.isOver()) return;
        if (stage == RefreshStage.IDLE) return;

        if (!InventoryUtil.getCurrentContainerName().equals("Brewing Stand")) {
            ChatUtil.warn("container name does not match \"Brewing Stand\"");
            disable();
            return;
        }

        switch (stage) {
            case TAKE_ITEM: {
                if (Minecraft.getMinecraft().thePlayer.openContainer.getSlot(BREWING_STAND_REFRESH_SLOT).getHasStack()) {
                    ChatUtil.log("found item at 1st slot");
                    InventoryUtil.leftClickItem(BREWING_STAND_REFRESH_SLOT);
                }

                scheduler.schedule(DelayUtil.getDelay(200, 200));
                break;
            }

            case PUT_ITEM_BACK: {
                if (!Minecraft.getMinecraft().thePlayer.openContainer.getSlot(BREWING_STAND_REFRESH_SLOT).getHasStack()) {
                    ChatUtil.log("item not found at 1st slot");
                    InventoryUtil.leftClickItem(BREWING_STAND_REFRESH_SLOT);
                }

                ChatUtil.warn("unable to find item");
                disable();
            }

            case CLOSE: {
                InventoryUtil.closeInventory();
                finish();
                break;
            }
        }
    }

    private enum RefreshStage {
        IDLE,
        TAKE_ITEM,
        PUT_ITEM_BACK,
        CLOSE,
    }

}
