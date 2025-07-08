package net.ethann.yogurtaddons.feature.impl.brewingstandrefiller.brewingstands;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
@Getter
public enum BrewingMaterial {
    RABBIT_FOOT(Items.rabbit_foot, 192, 176, 144),
    NETHER_WART(Items.nether_wart, 153, 0, 0),
    GLOWSTONE_BLOCK(Blocks.glowstone, 255, 236, 139);

    private final Item mcItem;
    private final int r;
    private final int g;
    private final int b;
    BrewingMaterial(Item mcItem, int r, int g, int b) {
        this.mcItem = mcItem;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    BrewingMaterial(Block block, int r, int g, int b) {
        this.mcItem = Item.getItemFromBlock(block);
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
