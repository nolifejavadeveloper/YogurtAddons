package net.ethann.yogurtaddons.feature.impl.potionrefiller.chestinput;

import net.ethann.yogurtaddons.feature.impl.potionrefiller.AutoPotionRefiller;
import net.ethann.yogurtaddons.feature.impl.potionrefiller.ChestNavigator;
import net.ethann.yogurtaddons.feature.impl.potionstasher.AutoPotionStasher;
import net.ethann.yogurtaddons.util.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class InputChestCommand extends CommandBase {

    private final InputChestEditor inputChestEditor;
    private final InputChestManager inputChestManager;

    public InputChestCommand(InputChestManager inputChestManager, InputChestRenderer renderer) {
        this.inputChestManager = inputChestManager;
        this.inputChestEditor = new InputChestEditor(inputChestManager, renderer);
    }


    @Override
    public String getCommandName() {
        return "mmi";
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
                inputChestEditor.toggle();
                break;
            }

            case "group": {
                if (args.length == 1) {
                    ChatUtil.warn("specify a group");
                    return;
                }

                if (args[1].equals("create")) {
                    if (args.length == 2) {
                        ChatUtil.warn("specify a group");
                        return;
                    }

                    if (inputChestManager.hasGroup(args[2])) {
                        ChatUtil.warn("group already exists");
                        return;
                    }

                    inputChestManager.createGroup(args[2]);
                    return;
                }

                if (inputChestManager.hasGroup(args[1])) {
                    inputChestManager.setCurrentGroup(args[1]);
                    ChatUtil.log("group set to " + args[1]);
                }else {
                    ChatUtil.warn("invalid group, use /group create <name> to create");
                }

                break;
            }

            default: {
                for (ChestPosition mode : ChestPosition.values()) {
                    if (mode.name().equalsIgnoreCase(args[0])) {
                        inputChestEditor.setMode(mode);
                        return;
                    }
                }

                ChatUtil.warn("invalid mode");
            }
        }
    }

    private void sendUsage() {
        ChatUtil.log("usage: /mmi edit");
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
