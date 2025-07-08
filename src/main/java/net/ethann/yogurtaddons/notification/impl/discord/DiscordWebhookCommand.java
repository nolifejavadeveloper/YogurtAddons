package net.ethann.yogurtaddons.notification.impl.discord;

import net.ethann.yogurtaddons.util.ChatUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.Objects;

public class DiscordWebhookCommand extends CommandBase {
    private final DiscordWebhookManager manager;

    public DiscordWebhookCommand(DiscordWebhookManager manager) {
        this.manager = manager;
    }

    @Override
    public String getCommandName() {
        return "dw";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/dw <-s|-u> <url>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            ChatUtil.log("current webhook service: " + manager.getWebhookUrl());
            return;
        }

        if (args.length == 2) {
            if (Objects.equals(args[0], "-s")) {
                manager.setServiceUrl(args[1]);
            }else if (Objects.equals(args[0], "-u")) {
                manager.setWebhookUrl(args[1]);
            }else {
                ChatUtil.log("invalid arg");
            }
            return;
        }

        ChatUtil.log(getCommandUsage(sender));
    }
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
