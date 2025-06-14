package net.ethann.yogurtaddons.macro.impl.potion;

import net.ethann.yogurtaddons.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class AutoSeller {
    private final Scheduler scheduler;
    private final AutoSellerHUD hud;
    private SellingStage stage = SellingStage.OPEN_CHEST;

    private final Queue<Pair<Integer, Integer>> order = new LinkedList<>(Arrays.asList(
            p(9, 54),
            p(18, 63),
            p(27, 72),
            p(0, 81),
            p(1, 82),
            p(28, 73),
            p(19, 64),
            p(10, 55),
            p(11, 56),
            p(20, 65),
            p(29, 74),
            p(2, 83),
            p(3, 84),
            p(30, 75),
            p(21, 66),
            p(12, 57),
            p(13, 58),
            p(22, 67),
            p(32, 76),
            p(4, 85),
            p(5, 86),
            p(33, 77),
            p(23, 68),
            p(14, 59),
            p(15, 60),
            p(24, 69),
            p(33, 78),
            p(6, 87),
            p(7, 88),
            p(34, 79),
            p(25, 70),
            p(16, 61),
            p(17, 62),
            p(26, 71),
            p(35, 80)
    ));

    private Pair<Integer, Integer> p(int a, int b) {
        return new Pair<>(a, b);
    }

    public AutoSeller() {
        this.scheduler = new Scheduler();
        this.hud = new AutoSellerHUD(scheduler);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        switch (stage) {
            case OPEN_CHEST:
                if (!scheduler.isOver()) return;
                ChatUtil.log("opening chest...");

                ControlUtil.rightClick();

                scheduler.schedule(DelayUtil.getDelay(400, 300));
                stage = SellingStage.SELECT_ITEM;
                break;
            case SELECT_ITEM: {
                if (!scheduler.isOver()) return;
                if (!InventoryUtil.getCurrentContainerName().equals("Large Chest")) {
                    ChatUtil.warn("container name does not match \"Large Chest\"");
                    disable();
                    return;
                }

                ChatUtil.log("selecting first item...");

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                Container chest = p.openContainer;

                Slot s = chest.getSlot(49);

                if (!s.getHasStack() || !shouldSell(s.getStack())) {
                   ChatUtil.warn("invalid slot for first click: " + s.getSlotIndex());
                   disable();
                   return;
                }

                ChatUtil.log("valid item for first click: " + s.getSlotIndex() + s.getStack().getDisplayName());
                InventoryUtil.leftClickItem(s.getSlotIndex());

                scheduler.schedule(DelayUtil.getDelay(400, 250));
                stage = SellingStage.FIRST_SHIFT_CLICK;
                break;
            }
            case FIRST_SHIFT_CLICK: {
                if (!scheduler.isOver()) return;
                if (!InventoryUtil.getCurrentContainerName().equals("Large Chest")) {
                    ChatUtil.warn("container name does not match \"Large Chest\"");
                    disable();
                    return;
                }

                ChatUtil.log("performing first shift click...");

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                Container chest = p.openContainer;

                Slot s = chest.getSlot(50);

                if (!s.getHasStack() || !shouldSell(s.getStack())) {
                    ChatUtil.warn("invalid slot for second click: " + s.getSlotIndex());
                    disable();
                    return;
                }

                ChatUtil.log("valid item for second click: " + s.getSlotIndex() + s.getStack().getDisplayName());
                InventoryUtil.leftClickItem(s.getSlotIndex());

                scheduler.schedule(DelayUtil.getDelay(100, 100));
                stage = SellingStage.ALL_TAKE;
                break;
            }
            case ALL_TAKE: {
                if (!scheduler.isOver()) return;
                if (!InventoryUtil.getCurrentContainerName().equals("Large Chest")) {
                    ChatUtil.warn("container name does not match \"Large Chest\"");
                    disable();
                    return;
                }

                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                Container chest = p.openContainer;

                ChatUtil.log("performing second shift click...");

                for (int i = 0; i < 9 * 6; i++) {
                    Slot s = chest.getSlot(i);
                    if (s.getHasStack() && shouldSell(s.getStack())) {
                        InventoryUtil.leftShiftClickItem(s.getSlotIndex());
                    }
                }

                scheduler.schedule(DelayUtil.getDelay(300, 200));
                stage = SellingStage.CLOSE_CHEST;
                break;
            }
            case CLOSE_CHEST: {
                if (!scheduler.isOver()) return;
                ChatUtil.log("done taking out, closing chest...");

                InventoryUtil.closeInventory();

                stage = SellingStage.OPEN_TRADE_MENU;
                scheduler.schedule(DelayUtil.getDelay(1200, 300));
                break;
            }
            case OPEN_TRADE_MENU: {
                if (!scheduler.isOver()) return;
                ChatUtil.sendCommand("trades");

                scheduler.schedule(DelayUtil.getDelay(900, 400));
                stage = SellingStage.SELL;
                break;
            }
            case SELL: {
                if (!scheduler.isOver()) return;
                if (order.isEmpty()) {
                    scheduler.schedule(DelayUtil.getDelay(400, 300));
                    stage = SellingStage.CLOSE_TRADES;
                    return;
                }
                if (!InventoryUtil.getCurrentContainerName().equals("Trades")) {
                    ChatUtil.warn("container name does not match \"Trades\"");
                    disable();
                    return;
                }
                EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
                InventoryPlayer inv = p.inventory;

                Pair<Integer,Integer> slot;
                while (true) {
                    if (order.isEmpty()) {
                        ChatUtil.log("unable to find a slot, ending...");
                        scheduler.schedule(DelayUtil.getDelay(400, 300));
                        stage = SellingStage.CLOSE_TRADES;
                        return;
                    }
                    slot = order.poll();
                    ItemStack item = inv.getStackInSlot(slot.a);
                    if (item == null) {
                        ChatUtil.log("skipping slot " + slot.a + " since it is empty");
                    }else if (!shouldSell(item)) {
                        ChatUtil.log("skipping slot " + slot.a + " since it is not the target item");
                    }else {
                        break;
                    }
                }
                InventoryUtil.leftShiftClickItem(slot.b);
                ChatUtil.log("clicked " + slot.b);

                scheduler.schedule(DelayUtil.getDelay(300, 100));
                break;
            }
            case CLOSE_TRADES: {
                if (!scheduler.isOver()) return;

                InventoryUtil.closeInventory();

                stage = SellingStage.IDLE;
                ChatUtil.log("all done, closing trade menu...");
                disable();
            }

        }
    }

    private boolean shouldSell(ItemStack itemStack) {
        return itemStack.getItem() == Items.potionitem;
    }

    public void enable() {
        ChatUtil.log("enabling autoseller");
        scheduler.schedule(2000);
        MinecraftForge.EVENT_BUS.register(this);
        hud.enable();
    }

    public void disable() {
        ChatUtil.log("disabling autoseller");
        MinecraftForge.EVENT_BUS.unregister(this);
        hud.disable();
    }


    private enum SellingStage {
        IDLE,
        OPEN_CHEST,
        SELECT_ITEM,
        FIRST_SHIFT_CLICK,
        ALL_TAKE,
        CLOSE_CHEST,
        OPEN_TRADE_MENU,
        SELL,
        CLOSE_TRADES,
    }
}
