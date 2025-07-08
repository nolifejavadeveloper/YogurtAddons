package net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.brewingstands;

import net.ethann.yogurtaddons.util.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class BrewingStandCommand extends CommandBase {

    private final BrewingStandEditor brewingStandEditor;

    public BrewingStandCommand(BrewingStandManager brewingStandManager, BrewingStandRenderer renderer) {
        this.brewingStandEditor = new BrewingStandEditor(brewingStandManager, renderer);
    }


    @Override
    public String getCommandName() {
        return "bsi";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            // toggle
            sendUsage();
            return;
        }

        switch (args[0]) {
            case "edit": {
                brewingStandEditor.toggle();
                break;
            }

            default: {
                for (BrewingMaterial mode : BrewingMaterial.values()) {
                    if (mode.name().equalsIgnoreCase(args[0])) {
                        brewingStandEditor.setMode(mode);
                        return;
                    }
                }

                ChatUtil.warn("invalid mode");
            }
        }
    }

    private void sendUsage() {
        ChatUtil.log("usage: /bsi edit");
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
