package net.ethann.yogurtaddons;

import net.ethann.yogurtaddons.commands.CrystalHollowsMapCommands;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = YogurtAddons.MOD_ID, useMetadata=true)
public class YogurtAddons {
    public static final String MOD_ID = "yogurtaddons";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new CrystalHollowsMapCommands());
    }

    public void registerListener(Object object) {

    }
}
