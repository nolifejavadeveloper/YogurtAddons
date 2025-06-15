package net.ethann.yogurtaddons.macro.impl.potion;

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

import java.util.regex.Pattern;

public class AutoChestRefiller {
    private static final String TARGET_ITEM = "Water Bottle";
    private static final Pattern REGEX_PATTERN;
    static {
        REGEX_PATTERN = Pattern.compile("^" + TARGET_ITEM + " x[0-9,]+$");
    }

    private final Scheduler scheduler;
    private final AutoSellerHUD hud;
    private RefillStage stage = RefillStage.OPEN_STASH;

    public AutoChestRefiller() {
        this.scheduler = new Scheduler();
        this.hud = new AutoSellerHUD(scheduler);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        switch (stage) {
            case OPEN_STASH: {
                if (!scheduler.isOver()) return;
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
                    disable();
                    return;
                }

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
                    disable();
                    return;
                }

                final int slot = 12;

                ChatUtil.log("performing first click...");

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                InventoryPlayer inv = p.inventory;

                ItemStack item = inv.getStackInSlot(slot);
                if (item == null || !isTarget(item)) {
                    ChatUtil.warn("invalid first click item on slot " + slot + ": " + item == null ? "null" : item.getDisplayName());
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
                    disable();
                    return;
                }

                final int slot = 13;

                ChatUtil.log("performing second shift click...");

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                InventoryPlayer inv = p.inventory;

                ItemStack item = inv.getStackInSlot(slot);
                if (item == null || !isTarget(item)) {
                    ChatUtil.warn("invalid second click item on slot " + slot + ": " + item == null ? "null" : item.getDisplayName());
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
                    disable();
                    return;
                }

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                Container chest = p.openContainer;
                InventoryPlayer inv = p.inventory;

                ChatUtil.log("dumping items into chest...");

                for (int i = 0; i < inv.getSizeInventory(); i++) {
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
                disable();

                stage = RefillStage.IDLE;
                break;
            }
        }
    }



    public void enable() {
        ChatUtil.log("enabling autochestrefiller");
        scheduler.schedule(2000);
        MinecraftForge.EVENT_BUS.register(this);
        hud.enable();
    }

    public void disable() {
        ChatUtil.log("disabling autochestrefiller");
        MinecraftForge.EVENT_BUS.unregister(this);
        hud.disable();
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
