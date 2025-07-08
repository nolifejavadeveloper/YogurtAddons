package net.ethann.yogurtaddons.feature.impl.potionrefiller;

import net.ethann.yogurtaddons.macro.impl.potion.AutoSellerHUD;
import net.ethann.yogurtaddons.notification.Notification;
import net.ethann.yogurtaddons.notification.NotificationService;
import net.ethann.yogurtaddons.notification.impl.desktop.DesktopNotificationService;
import net.ethann.yogurtaddons.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class AutoChestRefiller extends ManagedTask {
    private static final String TARGET_ITEM = "Water Bottle";
    private static final Pattern REGEX_PATTERN;
    private static final List<Integer> orderOfDump = new ArrayList<>(Arrays.asList(9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 0, 1, 2, 3, 4, 5, 6, 7));
    private static final int REPEAT = 2;
    static {
        REGEX_PATTERN = Pattern.compile("^" + TARGET_ITEM + " x[0-9,]+$");
    }

    private final Scheduler scheduler;
    private final AutoSellerHUD hud;
    private RefillStage stage;

    private boolean isInRetry = false;

    private boolean disableRepeats = false;

    private int currentOperations = 0;

    private final Consumer<Boolean> onFinish;

    public AutoChestRefiller(Consumer<Boolean> onFinish) {
        this.scheduler = new Scheduler();
        this.hud = new AutoSellerHUD(scheduler);

        this.onFinish = onFinish;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        switch (stage) {
            case OPEN_STASH: {
                if (!scheduler.isOver()) return;
                if (InventoryUtil.isPlayerInventoryFull()) {
                    ChatUtil.log("skipping stash due to full inventory");
                    stage = RefillStage.OPEN_CHEST;
                    return;
                }
                ChatUtil.log("opening stash menu...");

                ChatUtil.sendCommand("viewstash material");

                scheduler.schedule(DelayUtil.getDelay(500, 300));
                stage = RefillStage.TAKE_STASH;
                break;
            }
            case TAKE_STASH: {
                if (!scheduler.isOver()) return;
                if (!InventoryUtil.getCurrentContainerName().equals("View Stash")) {
                    ChatUtil.warn("container name does not match \"View Stash\"");
                    if (isInRetry) {
                        onFinish.accept(false);
                        disable();
                        return;
                    }

                    isInRetry = true;
                    scheduler.schedule(DelayUtil.getDelay(2000, 1000));
                    return;
                }

                isInRetry = false;

                ChatUtil.log("locating potion stash entry...");

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                Container chest = p.openContainer;

                boolean found = false;

                for (int i = 0; i < chest.getInventory().size() - 9; i++ ) {
                    Slot s = chest.getSlot(i);
                    if (s.getHasStack() && REGEX_PATTERN.matcher(EnumChatFormatting.getTextWithoutFormattingCodes(s.getStack().getDisplayName())).matches()) {
                        ChatUtil.log("found potion stash entry");
                        ChatUtil.log("clicking potion stash entry...");

                        InventoryUtil.leftClickItem(i);
                        found = true;

                        break;
                    }
                }

                if (!found) {
                    ChatUtil.warn("unable to find potion stash entry! did you run out?");
                    onFinish.accept(false);
                    disable();
                    return;
                }

                scheduler.schedule(DelayUtil.getDelay(400, 300));
                stage = RefillStage.CLOSE_STASH;
                break;
            }
            case CLOSE_STASH: {
                if (!scheduler.isOver()) return;
                ChatUtil.log("closing stash menu...");

                InventoryUtil.closeInventory();

                scheduler.schedule(DelayUtil.getDelay(300, 500));
                stage = RefillStage.OPEN_CHEST;

                break;
            }
            case OPEN_CHEST: {
                if (!scheduler.isOver()) return;
                ChatUtil.log("opening chest...");

                ControlUtil.rightClick();

                scheduler.schedule(DelayUtil.getDelay(500, 350));
                stage = RefillStage.FIRST_ITEM_SELECT;
                break;
            }
            case FIRST_ITEM_SELECT: {
                if (!scheduler.isOver()) return;
                if (!InventoryUtil.getCurrentContainerName().equals("Large Chest")) {
                    ChatUtil.warn("container name does not match \"Large Chest\"");
                    if (isInRetry) {
                        onFinish.accept(false);
                        disable();
                        return;
                    }

                    isInRetry = true;
                    scheduler.schedule(DelayUtil.getDelay(2000, 1000));
                    return;
                }

                isInRetry = false;

                if (InventoryUtil.isContainerFull(Minecraft.getMinecraft().thePlayer.openContainer)) {
                    ChatUtil.log("container is full, stopping refill");
                    disableRepeats = true;
                    stage = RefillStage.CLOSE_CHEST;
                    return;
                }

                final int slot = 12;

                ChatUtil.log("performing first click...");

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                InventoryPlayer inv = p.inventory;

                ItemStack item = inv.getStackInSlot(slot);
                if (item == null || !isTarget(item)) {
                    ChatUtil.warn("invalid first click item on slot " + slot + ": " + item == null ? "null" : item.getDisplayName());
                    onFinish.accept(false);
                    disable();
                    return;
                }

                InventoryUtil.leftClickItem(InventoryUtil.inventorySlotToProtocol(slot));

                scheduler.schedule(DelayUtil.getDelay(600, 400));
                stage = RefillStage.SECOND_ITEM_SHIFT;
                break;
            }

            case SECOND_ITEM_SHIFT: {
                if (!scheduler.isOver()) return;
                if (!InventoryUtil.getCurrentContainerName().equals("Large Chest")) {
                    ChatUtil.warn("container name does not match \"Large Chest\"");
                    if (isInRetry) {
                        onFinish.accept(false);
                        disable();
                        return;
                    }

                    isInRetry = true;
                    scheduler.schedule(DelayUtil.getDelay(2000, 1000));
                    return;
                }

                isInRetry = false;

                final int slot = 13;

                ChatUtil.log("performing second shift click...");

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                InventoryPlayer inv = p.inventory;

                ItemStack item = inv.getStackInSlot(slot);
                if (item == null || !isTarget(item)) {
                    ChatUtil.warn("invalid second click item on slot " + slot + ": " + (item == null ? "null" : item.getDisplayName()));
                    onFinish.accept(false);
                    disable();
                    return;
                }

                InventoryUtil.leftShiftClickItem(InventoryUtil.inventorySlotToProtocol(slot));

                scheduler.schedule(DelayUtil.getDelay(120, 90));
                stage = RefillStage.DUMP_ITEMS;
                break;
            }

            case DUMP_ITEMS: {
                if (!scheduler.isOver()) return;
                if (!InventoryUtil.getCurrentContainerName().equals("Large Chest")) {
                    ChatUtil.warn("container name does not match \"Large Chest\"");
                    if (isInRetry) {
                        onFinish.accept(false);
                        disable();
                        return;
                    }

                    isInRetry = true;
                    scheduler.schedule(DelayUtil.getDelay(2000, 1000));
                    return;
                }

                isInRetry = false;

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                InventoryPlayer inv = p.inventory;

                ChatUtil.log("dumping items into chest...");

                for (int i : orderOfDump) {
                    ItemStack s = inv.getStackInSlot(i);
                    if (s != null && isTarget(s)) {
                        InventoryUtil.leftShiftClickItem(InventoryUtil.inventorySlotToProtocol(i));
                    }
                }

                scheduler.schedule(DelayUtil.getDelay(300, 200));
                stage = RefillStage.CLOSE_CHEST;
                break;
            }

            case CLOSE_CHEST: {
                if (!scheduler.isOver()) return;
                ChatUtil.log("all done, closing chest");

                InventoryUtil.closeInventory();

                stage = RefillStage.IDLE;

                finish();
                break;
            }
        }
    }



    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);

        disableRepeats = false;

        hud.enable();
        currentOperations = 0;

        start();
    }

    private void start() {
        scheduler.schedule(DelayUtil.getDelay(1000, 500));
        stage = RefillStage.OPEN_STASH;
    }

    private void finish() {
        if (disableRepeats) {
            ChatUtil.log("force finishing");
            forceFinish();
            return;
        }

        start();
    }

    private void forceFinish() {
        disable();
//        onFinish.accept(true);
    }

    public void onDisable() {
        hud.disable();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    protected String getName() {
        return "Potion Refiller";
    }

    private boolean isTarget(ItemStack stack) {
        return stack.getItem() == Items.potionitem;
    }


    private enum RefillStage {
        IDLE,
        OPEN_STASH,
        TAKE_STASH,
        CLOSE_STASH,
        OPEN_CHEST,
        FIRST_ITEM_SELECT,
        SECOND_ITEM_SHIFT,
        DUMP_ITEMS,
        CLOSE_CHEST
    }
}
