package net.ethann.yogurtaddons.feature.impl.potionstasher;

import net.ethann.yogurtaddons.feature.impl.potionrefiller.AutoPotionRefiller;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class AutoPotionStasherCommand extends CommandBase {
    private final AutoPotionStasher feature;

    public AutoPotionStasherCommand(AutoPotionStasher feature) {
        this.feature = feature;
    }

    @Override
    public String getCommandName() {
        return "potionst";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        feature.enable();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}