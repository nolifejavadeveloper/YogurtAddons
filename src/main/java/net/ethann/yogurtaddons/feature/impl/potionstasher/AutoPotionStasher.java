package net.ethann.yogurtaddons.feature.impl.potionstasher;

import net.ethann.yogurtaddons.notification.Notification;
import net.ethann.yogurtaddons.notification.NotificationService;
import net.ethann.yogurtaddons.notification.impl.desktop.DesktopNotificationService;
import net.ethann.yogurtaddons.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoPotionStasher extends ManagedTask {
    private static final int FILLING_SLOT = 0;
    private static final ControlUtil.Key FILLING_KEY = ControlUtil.Key.SLOT_1;
    private static final int ABIPHONE_SLOT = 7;
    private static final ControlUtil.Key ABIPHONE_KEY = ControlUtil.Key.SLOT_8;
    private static final int GLASS_BOTTLE_SLOT = 12;
    private static final int GLASS_BOTTLE_STACK_OPTION = 24;

    private boolean isInRetry = false;

    private final List<Integer> slotOrder = new ArrayList<>(Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16, 17,
            26, 25, 24, 23, 22, 21, 20, 19, 18,
            27, 28, 29, 30, 31, 32, 33, 34, 35));
    private int index;

    private final Scheduler scheduler;

    private RefillStage stage;

    public AutoPotionStasher() {
        this.scheduler = new Scheduler();

        ClientCommandHandler.instance.registerCommand(new AutoPotionStasherCommand(this));
    }

    protected void onEnable() {
        stage = RefillStage.OPEN_INVENTORY;
        index = 0;

        scheduler.schedule(1000);
        MinecraftForge.EVENT_BUS.register(this);
    }

    protected void finish() {
        disable();
    }

    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    protected String getName() {
        return "Potion Stasher";
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (!scheduler.isOver()) return;

        switch (stage) {
            case SELECT_ABIPHONE_SLOT: {
                ChatUtil.log("switching to slot: " + ABIPHONE_SLOT + " with key " + ABIPHONE_KEY);
                InventoryUtil.setHoldingSlot(ABIPHONE_SLOT);
                scheduler.schedule(DelayUtil.getDelay(300, 250));

                stage = RefillStage.OPEN_ABIPHONE;

                break;
            }
            case OPEN_ABIPHONE: {
                ChatUtil.log("opening abiphone");

                ControlUtil.rightClick();

                scheduler.schedule(DelayUtil.getDelay(1200, 250));
                stage = RefillStage.CALL_WITCH;
                break;
            }
            case CALL_WITCH: {
                ChatUtil.log("attempting to find witch contact");
                Container chest = Minecraft.getMinecraft().thePlayer.openContainer;
                for (int i = 0; i < chest.getInventory().size() - 36; i++) {
                    Slot s = chest.getSlot(i);
                    if (s.getHasStack() && StringUtils.stripControlCodes(s.getStack().getDisplayName()).equalsIgnoreCase("alchemist")) {
                        ChatUtil.log("witch contact found");
                        InventoryUtil.leftClickItem(i);

                        scheduler.schedule(DelayUtil.getDelay(5000, 250));
                        stage = RefillStage.OPEN_ITEM_OPTIONS;
                        return;
                    }
                }

                ChatUtil.warn("no witch contact found");
                disable();

                break;
            }
            case OPEN_ITEM_OPTIONS: {
                ChatUtil.log("opening details");
                InventoryUtil.rightClickItem(GLASS_BOTTLE_SLOT);
                scheduler.schedule(DelayUtil.getDelay(500, 250));

                stage = RefillStage.BUY_ITEMS;

                break;
            }
            case BUY_ITEMS: {
                if (InventoryUtil.isPlayerInventoryFull()) {
                    ChatUtil.log("inventory full, closing...");
                    scheduler.schedule(DelayUtil.getDelay(250, 250));
                    stage = RefillStage.CLOSE_ABIPHONE;
                }

                ChatUtil.log("buying item...");

                InventoryUtil.middleClickItem(GLASS_BOTTLE_STACK_OPTION);
                scheduler.schedule(DelayUtil.getDelay(175, 100));
                break;
            }
            case CLOSE_ABIPHONE: {
                InventoryUtil.closeInventory();
                scheduler.schedule(DelayUtil.getDelay(300, 300));

                stage = RefillStage.SELECT_MAIN_SLOT;
                break;
            }

            case SELECT_MAIN_SLOT: {
                ChatUtil.log("selecting main slot");
                InventoryUtil.setHoldingSlot(FILLING_SLOT);
                scheduler.schedule(DelayUtil.getDelay(200, 200));

                stage = RefillStage.OPEN_INVENTORY;
                break;
            }

            case OPEN_INVENTORY: {
                Minecraft.getMinecraft().displayGuiScreen(new GuiInventory(Minecraft.getMinecraft().thePlayer));
                scheduler.schedule(DelayUtil.getDelay(400, 100));

                stage = RefillStage.SWITCH_SLOTS;
                break;
            }

            case SWITCH_SLOTS: {
                ChatUtil.log("switching slots...");
                while (true) {
                    if (index >= slotOrder.size()) {
                        index = 0;
                        if (InventoryUtil.isPlayerInventoryFull()) {
                            scheduler.schedule(DelayUtil.getDelay(400, 200));
                            ChatUtil.warn("no slots left, back to throwing then buying");
                            stage = RefillStage.THROW_INVENTORY;
                            return;
                        }else {
                            scheduler.schedule(DelayUtil.getDelay(750, 400));
                            ChatUtil.warn("no slots left, back to buying");
                            InventoryUtil.closeInventory();
                            stage = RefillStage.SELECT_ABIPHONE_SLOT;
                        }
                        return;
                    }

                    int slotIndex = slotOrder.get(index);

                    ItemStack s = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(slotIndex);
                    index++;
                    if (s == null || s.getItem() != Items.glass_bottle) {
                        continue;
                    }

                    ChatUtil.log("slot found: " + slotIndex + " -> " + FILLING_SLOT);

                    InventoryUtil.swapItem(slotIndex, FILLING_SLOT);

                    scheduler.schedule(DelayUtil.getDelay(300, 200));

                    stage = RefillStage.CLOSE_INVENTORY;
                    break;
                }
            }

            case CLOSE_INVENTORY: {
                ChatUtil.log("closing inventory");
                InventoryUtil.closeInventory();

                scheduler.schedule(DelayUtil.getDelay(400, 250));
                stage = RefillStage.START_FILL;
                break;
            }

            case START_FILL: {
                ChatUtil.log("filling bottles");
                ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(FILLING_SLOT);
                if (stack == null) {
                    ChatUtil.error("stack at slot 1 is null, unknown reason");
                    disable();

                    return;
                }

                if (stack.getItem() == Items.potionitem) {
                    // full
                    if (!isInRetry) {
                        ChatUtil.error("this slot is already finished, retrying...");
                        scheduler.schedule(3000);
                        isInRetry = true;
                        return;
                    }
                    ChatUtil.error("retry used, disabling...");

                    disable();

                    return;
                }

                isInRetry = false;

                ControlUtil.hold(ControlUtil.Key.USE_ITEM);

                stage = RefillStage.END_FILL;
                break;
            }

            case END_FILL: {
                ItemStack stack = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(FILLING_SLOT);
                if (stack == null) {
                    ChatUtil.error("stack at slot 1 is null, unknown reason");
                    disable();

                    return;
                }

                if (stack.getItem() == Items.potionitem) {
                    ChatUtil.log("filling completed");
                    // full
                    ControlUtil.release(ControlUtil.Key.USE_ITEM);
                    scheduler.schedule(DelayUtil.getDelay(100, 200));

                    stage = RefillStage.OPEN_INVENTORY;
                }

                break;
            }

            case THROW_INVENTORY: {
                while (true) {
                    if (index >= slotOrder.size()) {
                        InventoryUtil.closeInventory();
                        ChatUtil.log("threw everything out, restarting purchase");
                        scheduler.schedule(DelayUtil.getDelay(500, 200));
                        stage = RefillStage.SELECT_ABIPHONE_SLOT;
                        index = 0;
                        return;
                    }

                    int slotIndex = slotOrder.get(index);

                    ItemStack s = Minecraft.getMinecraft().thePlayer.inventory.getStackInSlot(slotIndex);
                    index++;
                    if (s == null || s.getItem() != Items.potionitem) {
                        continue;
                    }

                    ChatUtil.log("slot found: " + slotIndex + " throwing...");

                    InventoryUtil.dropItem(slotIndex);
                    break;
                }

                scheduler.schedule(DelayUtil.getDelay(150, 100));
                break;
            }
        }
    }

    enum RefillStage {
        SELECT_ABIPHONE_SLOT,
        OPEN_ABIPHONE,
        CALL_WITCH,
        OPEN_ITEM_OPTIONS,
        BUY_ITEMS,
        CLOSE_ABIPHONE,
        SELECT_MAIN_SLOT,
        OPEN_INVENTORY,
        SWITCH_SLOTS,
        CLOSE_INVENTORY,
        CLOSE_INVENTORY_THEN_BUY,
        START_FILL,
        END_FILL,
        THROW_INVENTORY_START,
        THROW_INVENTORY,
    }
}
