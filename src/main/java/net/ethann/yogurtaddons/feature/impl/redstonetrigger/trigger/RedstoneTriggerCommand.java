package net.ethann.yogurtaddons.feature.impl.redstonetrigger.trigger;

import net.ethann.yogurtaddons.util.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class RedstoneTriggerCommand extends CommandBase {

    private final TriggerEditor triggerEditor;
    private final TriggerManager triggerManager;

    public RedstoneTriggerCommand(TriggerManager triggerManager, TriggerRenderer renderer) {
        this.triggerManager = triggerManager;
        this.triggerEditor = new TriggerEditor(triggerManager, renderer);
    }


    @Override
    public String getCommandName() {
        return "rti";
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
                triggerEditor.toggle();
                break;
            }

            case "group": {
                if (args.length == 1) {
                    ChatUtil.warn("specify a group");
                    return;
                }

                if (args[1].equals("name")) {
                    if (args.length == 2) {
                        ChatUtil.warn("specify a name");
                        return;
                    }

                    triggerEditor.setNextName(args[2]);
                    return;
                }
                break;
            }

            default: {
                for (TriggerType mode : TriggerType.values()) {
                    if (mode.name().equalsIgnoreCase(args[0])) {
                        triggerEditor.setMode(mode);
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
