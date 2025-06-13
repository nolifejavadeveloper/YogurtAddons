package net.ethann.yogurtaddons.macro.impl.potion;

import net.ethann.yogurtaddons.util.InventoryUtil;
import net.ethann.yogurtaddons.util.Pair;
import net.ethann.yogurtaddons.util.Scheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class AutoSellChest {
    private final Scheduler scheduler;
    private final AutoSellChestHUD hud;

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

    public AutoSellChest() {
        this.scheduler = new Scheduler();
        this.hud = new AutoSellChestHUD(scheduler);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        if (!scheduler.isOver()) return;
        if (order.isEmpty()) {
            disable();
            return;
        }
        EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
        InventoryPlayer inv = p.inventory;

        Pair<Integer,Integer> slot;
        while (true) {
            if (order.isEmpty()) {
                log("unable to find a slot, ending...");
                disable();
                return;
            }
            slot = order.poll();
            ItemStack item = inv.getStackInSlot(slot.a);
            if (item == null) {
                log("skipping slot " + slot.a + " since it is empty");
            }else if (!shouldSell(item)) {
                log("skipping slot " + slot.a + " since it is not the target item");
            }else {
                break;
            }
        }
        InventoryUtil.leftShiftClickItem(slot.b);
        log("clicked " + slot.b);

        scheduler.schedule(generateDelay());
    }

    private boolean shouldSell(ItemStack itemStack) {
        return itemStack.getItem() == Items.potionitem;
    }

    public void enable() {
        log("enabling autoseller");
        scheduler.schedule(1000);
        MinecraftForge.EVENT_BUS.register(this);
        hud.enable();
    }

    public void disable() {
        log("disabling autoseller");
        MinecraftForge.EVENT_BUS.unregister(this);
        hud.disable();
    }

    private int generateDelay() {
        return 300 + (int) (Math.random() * 100);
    }

    private void log(String s) {
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("§a§lYo§e§lgurt§r§f > " + s));
    }
}
