package net.ethann.yogurtaddons.feature.impl.chestseller;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class AutoChestSellerCommand extends CommandBase {
    private final AutoChestSeller feature;

    public AutoChestSellerCommand(AutoChestSeller feature) {
        this.feature = feature;
    }

    @Override
    public String getCommandName() {
        return "potioncs";
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
