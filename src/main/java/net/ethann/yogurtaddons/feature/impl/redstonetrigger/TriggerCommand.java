package net.ethann.yogurtaddons.feature.impl.redstonetrigger;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class TriggerCommand extends CommandBase {

    private final RedstoneTrigger main;

    public TriggerCommand(RedstoneTrigger main) {
        this.main = main;
    }

    @Override
    public String getCommandName() {
        return "rt";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        main.toggle();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
