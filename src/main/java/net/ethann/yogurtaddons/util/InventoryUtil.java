package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
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

    public static void setHolderingSlot(int slot) {
        Validate.isTrue(slot >= 0 && slot <= 8, "slot must be between 0 and 8");
        mc.thePlayer.inventory.currentItem = slot;
    }

    public static void doubleClickItem(int slot) {
        windowClick(slot, MOUSE_CLICK_LEFT, MODE_DOUBLE_CLICK);
    }

    public static void leftClickItem(int slot) {
        windowClick(slot, MOUSE_CLICK_LEFT, MODE_REGULAR_CLICK);
    }

    public static void leftShiftClickItem(int slot) {
        windowClick(slot, MOUSE_CLICK_LEFT, MODE_SHIFT_CLICK);
    }


    public static void middleClickItem(int slot) {
        windowClick(slot, MOUSE_CLICK_MIDDLE, MODE_MIDDLE_CLICK);
    }

    public static String getCurrentContainerName() {
        GuiScreen currentScreen = mc.currentScreen;
        if (currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) currentScreen;
            IInventory inv = ((ContainerChest) chest.inventorySlots).getLowerChestInventory();
            System.out.println(inv.getDisplayName().getUnformattedText());
            return inv.getDisplayName().getUnformattedText();
        }
        return "";
    }

    public static void closeInventory() {
        mc.thePlayer.closeScreen();
    }

    private static void windowClick(int slot, int mouse, int mode) {
        GuiScreen currentScreen = mc.currentScreen;
        if (currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) currentScreen;
            Container c = chest.inventorySlots;
            PlayerControllerMP player = mc.playerController;
            player.windowClick(c.windowId, slot, mouse, mode, mc.thePlayer);
        }
    }

}
