package net.ethann.yogurtaddons.feature.impl.brewingstandrefiller;

import net.ethann.yogurtaddons.feature.common.UsedBlockRenderer;
import net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.brewingstands.BrewingMaterial;
import net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.brewingstands.BrewingStandCommand;
import net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.brewingstands.BrewingStandManager;
import net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.brewingstands.BrewingStandRenderer;
import net.ethann.yogurtaddons.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class BrewingStandRefiller extends Task{
    private static final int BREWING_STAND_ITEM_SLOT = 13;

    private final List<BlockPos> finishedStands;
    private final UsedBlockRenderer renderer;
    private final Scheduler scheduler;
    private final BrewingStandManager brewingStandManager;

    private BlockPos lastBrewingStandPos;

    private long lastClick;

    private RefillStage stage;

    private boolean enabled = false;

    public BrewingStandRefiller() {
        this.scheduler = new Scheduler();
        this.finishedStands = new ArrayList<>();
        this.renderer = new UsedBlockRenderer(finishedStands);
        this.brewingStandManager = new BrewingStandManager();

        lastClick = System.currentTimeMillis();

        ClientCommandHandler.instance.registerCommand(new BrewingStandRefillerCommand(this));
        ClientCommandHandler.instance.registerCommand(new BrewingStandCommand(brewingStandManager, new BrewingStandRenderer(brewingStandManager)));
    }

    public void onEnable() {
        enabled = true;
        finishedStands.clear();
        stage = RefillStage.IDLE;
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
        return "Brewing Stand Refiller";
    }

    private void start() {
        scheduler.schedule(DelayUtil.getDelay(700, 250));
        stage = RefillStage.TAKE_EXISTING;
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
        stage = RefillStage.IDLE;
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
                lastBrewingStandPos = event.pos;
                lastClick = System.currentTimeMillis();
                start();
                return;
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (!scheduler.isOver()) return;
        if (stage == RefillStage.IDLE) return;

        if (!InventoryUtil.getCurrentContainerName().equals("Brewing Stand")) {
            ChatUtil.warn("container name does not match \"Brewing Stand\"");
            disable();
            return;
        }

        switch (stage) {
            case TAKE_EXISTING: {
                ChatUtil.log("attempting to take out");
                Slot materialSlot = Minecraft.getMinecraft().thePlayer.openContainer.getSlot(BREWING_STAND_ITEM_SLOT);

                if (materialSlot.getHasStack()) {
                    ChatUtil.log("found, taking out");
                    InventoryUtil.leftShiftClickItem(BREWING_STAND_ITEM_SLOT);
                    scheduler.schedule(DelayUtil.getDelay(500, 200));
                }

                stage = RefillStage.FILL;

                break;
            }
            case FILL: {
                InventoryPlayer inventory = Minecraft.getMinecraft().thePlayer.inventory;

                boolean found = false;
                for (int i = 0; i < inventory.getSizeInventory(); i++) {
                    ItemStack stack = inventory.getStackInSlot(i);
                    if (shouldUseItem(stack)) {
                        ChatUtil.log("item found at slot " + i);
                        InventoryUtil.leftShiftClickItem(InventoryUtil.inventorySlotToProtocol(i));

                        found = true;
                        break;
                    }
                }

                if (!found) {
                    ChatUtil.warn("unable to locate material in inventory");
                    finish();
                    return;
                }

                scheduler.schedule(DelayUtil.getDelay(300, 200));
                stage = RefillStage.CLOSE;

                break;
            }

            case CLOSE: {
                InventoryUtil.closeInventory();
                finish();
                break;
            }
        }
    }

    private enum RefillStage {
        IDLE,
        TAKE_EXISTING,
        FILL,
        CLOSE,
    }

    private boolean shouldUseItem(ItemStack stack) {
        BrewingMaterial mat = brewingStandManager.getMaterialAtPos(lastBrewingStandPos);
        if (mat == null) {
            ChatUtil.warn("no material set for this block :(");
            return false;
        }
        return stack != null && stack.getItem() == mat.getMcItem() && stack.stackSize == 64;
    }
}
