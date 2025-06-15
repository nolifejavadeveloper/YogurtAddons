package net.ethann.yogurtaddons.macro.impl.potion.chestinput;

import net.ethann.yogurtaddons.util.ChatUtil;
import net.ethann.yogurtaddons.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class InputChestCommand extends CommandBase {

    private final InputChestManager inputChestManager;
    private final InputChestRenderer renderer;

    private boolean showing = false;

    public InputChestCommand(InputChestManager inputChestManager, InputChestRenderer renderer) {
        this.inputChestManager = inputChestManager;
        this.renderer = renderer;
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
        if (args.length != 1) {
            // toggle
            ChatUtil.log("usage: /mmi <add|remove>");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "add": {
                BlockPos pos = getChestLookingAt();
                if (pos == null) return;

                inputChestManager.addChest(pos);
                break;
            }
            case "remove": {
                BlockPos pos = getChestLookingAt();
                if (pos == null) return;

                inputChestManager.remove(pos);
                break;
            }
            case "show": {
                if (showing) {
                    renderer.stopRendering();
                    ChatUtil.log("stopped showing input chests");
                }else {
                    renderer.setRenderingType(InputChestRenderer.RenderType.ALL);
                    renderer.startRendering();
                    ChatUtil.log("started showing input chests");
                }

                break;
            }
        }
    }

    private BlockPos getChestLookingAt() {
        BlockPos pos = PlayerUtil.getBlockLookingAt();
        Block b = Minecraft.getMinecraft().thePlayer.getEntityWorld().getBlockState(pos).getBlock();
        if (b == Blocks.chest || b == Blocks.trapped_chest) {
            return pos;
        }else {
            ChatUtil.log("please select a chest");
        }

        return null;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }
}
