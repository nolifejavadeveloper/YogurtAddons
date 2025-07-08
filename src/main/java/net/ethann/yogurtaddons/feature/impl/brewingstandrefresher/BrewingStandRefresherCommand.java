package net.ethann.yogurtaddons.feature.impl.brewingstandrefresher;

import net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.BrewingStandRefiller;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class BrewingStandRefresherCommand extends CommandBase {
    private final BrewingStandRefresher feature;

    public BrewingStandRefresherCommand(BrewingStandRefresher feature) {
        this.feature = feature;
    }

    @Override
    public String getCommandName() {
        return "potionref";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        feature.toggle();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
