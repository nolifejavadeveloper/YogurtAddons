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

public class AutoPotionRefiller {
    private final Scheduler scheduler;
    private final AutoSellerHUD hud;
    private RefillStage stage = RefillStage.OPEN_STASH;

    public AutoPotionRefiller() {
        this.scheduler = new Scheduler();
        this.hud = new AutoSellerHUD(scheduler);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        switch (stage) {

        }
    }



    public void enable() {
        ChatUtil.log("enabling autorefiller");
        scheduler.schedule(2000);
        MinecraftForge.EVENT_BUS.register(this);
        hud.enable();
    }

    public void disable() {
        ChatUtil.log("disabling autorefiller");
        MinecraftForge.EVENT_BUS.unregister(this);
        hud.disable();
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
