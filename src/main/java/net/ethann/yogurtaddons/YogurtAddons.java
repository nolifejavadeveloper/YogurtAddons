package net.ethann.yogurtaddons;

import lombok.Getter;
import net.ethann.yogurtaddons.commands.CrystalHollowsMapCommands;
import net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.BrewingStandRefiller;
import net.ethann.yogurtaddons.feature.impl.brewingstandrefresher.BrewingStandRefresher;
import net.ethann.yogurtaddons.feature.impl.chestseller.AutoChestSeller;
import net.ethann.yogurtaddons.feature.impl.potionrefiller.AutoPotionRefiller;
import net.ethann.yogurtaddons.feature.impl.potionstasher.AutoPotionStasher;
import net.ethann.yogurtaddons.feature.impl.redstonetrigger.RedstoneTrigger;
import net.ethann.yogurtaddons.notification.Notification;
import net.ethann.yogurtaddons.notification.NotificationLevel;
import net.ethann.yogurtaddons.notification.NotificationService;
import net.ethann.yogurtaddons.notification.impl.desktop.DesktopNotificationService;
import net.ethann.yogurtaddons.task.TaskManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = YogurtAddons.MOD_ID, useMetadata=true)
@Getter
public class YogurtAddons {
    public static final String MOD_ID = "yogurtaddons";
    @Getter
    private static YogurtAddons instance;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new CrystalHollowsMapCommands());
        new AutoPotionRefiller();
        new AutoPotionStasher();
        new AutoChestSeller();
        new BrewingStandRefiller();
        new BrewingStandRefresher();
        new RedstoneTrigger();

        NotificationService.getInstance().send(new Notification().withTitle("Instance Started").withDetails("Make sure you can see this message!").withLevel(NotificationLevel.INFO), NotificationService.DISCORD);

        instance = this;
    }


    public void registerListener(Object object) {

    }
}
