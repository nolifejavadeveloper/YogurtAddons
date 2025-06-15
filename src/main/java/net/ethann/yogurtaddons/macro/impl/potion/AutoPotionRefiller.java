package net.ethann.yogurtaddons.macro.impl.potion;

import net.ethann.yogurtaddons.macro.impl.potion.chestinput.InputChestCommand;
import net.ethann.yogurtaddons.macro.impl.potion.chestinput.InputChestManager;
import net.ethann.yogurtaddons.macro.impl.potion.chestinput.InputChestRenderer;
import net.minecraftforge.client.ClientCommandHandler;

public class AutoPotionRefiller {
    private final InputChestManager inputChestManager;
    private final InputChestRenderer inputChestRenderer;

    public AutoPotionRefiller() {
        this.inputChestManager = new InputChestManager();
        this.inputChestRenderer = new InputChestRenderer(null, inputChestManager);

        ClientCommandHandler.instance.registerCommand(new InputChestCommand(inputChestManager, inputChestRenderer));
    }
}
