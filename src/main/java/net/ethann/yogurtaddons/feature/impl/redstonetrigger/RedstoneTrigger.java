package net.ethann.yogurtaddons.feature.impl.redstonetrigger;

import net.ethann.yogurtaddons.feature.impl.potionstasher.AutoPotionStasherCommand;
import net.ethann.yogurtaddons.feature.impl.redstonetrigger.trigger.*;
import net.ethann.yogurtaddons.notification.Notification;
import net.ethann.yogurtaddons.notification.NotificationLevel;
import net.ethann.yogurtaddons.notification.NotificationService;
import net.ethann.yogurtaddons.notification.impl.desktop.DesktopNotificationService;
import net.ethann.yogurtaddons.notification.impl.discord.DiscordNotificationService;
import net.ethann.yogurtaddons.util.ChatUtil;
import net.ethann.yogurtaddons.util.Task;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RedstoneTrigger extends Task {
    private final TriggerManager triggerManager;
    private final TriggerRenderer triggerRenderer;

    private boolean enabled = false;

    public RedstoneTrigger() {
        triggerManager = new TriggerManager();
        triggerRenderer = new TriggerRenderer(triggerManager);
        ClientCommandHandler.instance.registerCommand(new RedstoneTriggerCommand(triggerManager, triggerRenderer));
        ClientCommandHandler.instance.registerCommand(new TriggerCommand(this));
    }

    protected void onEnable() {
        triggerRenderer.enable();
        MinecraftForge.EVENT_BUS.register(this);

        enabled = true;
    }

    protected void onDisable() {
        triggerRenderer.disable();
        MinecraftForge.EVENT_BUS.unregister(this);

        enabled = false;
    }

    @Override
    protected String getName() {
        return "Redstone Trigger Service";
    }

    public void toggle() {
        if (enabled) {
            disable();
        }else {
            enable();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        for (Trigger t : triggerManager.getAllTriggers()) {
            World world =  Minecraft.getMinecraft().thePlayer.getEntityWorld();
            if (world == null) return;
            IBlockState state = world.getBlockState(t.getBlockPos());
            if (state == null) return;
            if (state.getBlock() == Blocks.redstone_wire) {
                boolean isLit = state.getValue(BlockRedstoneWire.POWER) > 0;

                if (isLit && t.getType() == TriggerType.OFF || !isLit && t.getType() == TriggerType.ON) {
                    if (!t.isReady()) {
                        t.setReady(true);
                    }

                    return;
                }

                if (!t.isReady()) return;

                ChatUtil.log("trigger " + t.getName()  + " triggered!");
                NotificationService.getInstance().send(Notification.create().withTitle("Redstone Trigger Activated").withDetails("The following redstone trigger has been triggered: **" + t.getType() + "**").withLevel(NotificationLevel.WARNING), DesktopNotificationService.class, DiscordNotificationService.class);
                t.setReady(false);
            }
        }
    }

}
