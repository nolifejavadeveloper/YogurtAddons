package net.ethann.yogurtaddons.feature.impl.potionrefiller.chestinput;

import lombok.AllArgsConstructor;
import net.minecraft.util.BlockPos;

@AllArgsConstructor
public class ChestInput {
    public BlockPos pos;
    public ChestPosition orientation;
}
