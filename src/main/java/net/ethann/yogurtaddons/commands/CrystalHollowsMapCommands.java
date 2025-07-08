package net.ethann.yogurtaddons.commands;

import net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.BrewingStandRefiller;
import net.ethann.yogurtaddons.feature.impl.chestseller.AutoChestSeller;
import net.ethann.yogurtaddons.feature.impl.potionrefiller.AutoPotionRefiller;
import net.ethann.yogurtaddons.feature.impl.potionstasher.AutoPotionStasher;
import net.ethann.yogurtaddons.util.CameraMover;
import net.ethann.yogurtaddons.util.InventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class CrystalHollowsMapCommands extends CommandBase {
    CameraMover cameraMover;

    @Override
    public String getCommandName() {
        return "chm";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            // toggle
            new AutoChestSeller().enable();
            return;
        }

        if (args.length == 2) {
            // toggle
            new AutoPotionRefiller().enable();
            return;
        }

        if (args.length == 3) {
            // toggle
            new AutoPotionStasher().enable();
            return;
        }

        if (args.length == 1) {
            // toggle
            cameraMover = new CameraMover(100.0f, 20.0f);
            MinecraftForge.EVENT_BUS.register(this);
            return;
        }


        if (args.length == 4) {
            new BrewingStandRefiller().enable();
        }
    }
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        cameraMover.tick();
    }
}
