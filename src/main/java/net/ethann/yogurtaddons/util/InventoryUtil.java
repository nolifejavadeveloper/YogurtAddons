package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.Validate;

public class InventoryUtil {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static int MOUSE_CLICK_LEFT = 0;
    public static int MOUSE_CLICK_RIGHT = 1;
    public static int MOUSE_CLICK_MIDDLE = 2;

    public static int MODE_REGULAR_CLICK = 0;
    public static int MODE_SHIFT_CLICK = 1;
    public static int MODE_HOTBAR_SWAP = 2;
    public static int MODE_MIDDLE_CLICK = 3;
    public static int MODE_DROP = 4;
    public static int MODE_DRAG = 5;
    public static int MODE_DOUBLE_CLICK = 6;

    public static void setHoldingSlot(int slot) {
        Validate.isTrue(slot >= 0 && slot <= 8, "slot must be between 0 and 8");
        mc.thePlayer.inventory.currentItem = slot;
    }

    public static void rightClickItem(int slot) {
        windowClick(slot, MOUSE_CLICK_RIGHT, MODE_REGULAR_CLICK);
    }

    public static void leftClickItem(int slot) {
        windowClick(slot, MOUSE_CLICK_LEFT, MODE_REGULAR_CLICK);
    }

    public static void leftShiftClickItem(int slot) {
        windowClick(slot, MOUSE_CLICK_LEFT, MODE_SHIFT_CLICK);
    }

    public static void swapItem(int slot, int key) {
        windowClick(slot, key, MODE_HOTBAR_SWAP);
    }


    public static void middleClickItem(int slot) {
        windowClick(slot, MOUSE_CLICK_MIDDLE, MODE_MIDDLE_CLICK);
    }

    public static void dropItem(int slot) {
        windowClick(slot, 0, MODE_DROP);
    }

    public static void dropStack(int slot) {
        windowClick(slot, 1, MODE_DROP);
    }

    public static void holdItem(int slot) {
        mc.thePlayer.inventory.currentItem = slot;
    }

    public static String getCurrentContainerName() {
        GuiScreen currentScreen = mc.currentScreen;
        if (currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) currentScreen;
            IInventory inv = ((ContainerChest) chest.inventorySlots).getLowerChestInventory();
            return inv.getDisplayName().getUnformattedText();
        }
        return "";
    }

    public static boolean isContainerFull(Container container) {
        for (int i = 0; i < container.getInventory().size() - 36; i++) {
            if (!container.getSlot(i).getHasStack()) return false;
        }

        return true;
    }

    public static boolean isPlayerInventoryFull() {
        InventoryPlayer inventoryPlayer = mc.thePlayer.inventory;
        for (int i = 0; i < 36; i++) {
            if (inventoryPlayer.getStackInSlot(i) == null) return false;
        }

        return true;
    }

    public static void closeInventory() {
        mc.thePlayer.closeScreen();
    }

    public static int inventorySlotToProtocol(int slot) {
        return slot + (slot > 8 ? 45 : 81);
    }

    private static void windowClick(int slot, int mouse, int mode) {
        GuiScreen currentScreen = mc.currentScreen;
        if (currentScreen instanceof GuiChest || currentScreen instanceof GuiInventory) {
            GuiContainer chest = (GuiContainer) currentScreen;
            Container c = chest.inventorySlots;
            PlayerControllerMP player = mc.playerController;
            player.windowClick(c.windowId, slot, mouse, mode, mc.thePlayer);
        }else {
            ChatUtil.error("unable to click window, not in gui");
        }
    }
}
