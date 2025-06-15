package net.ethann.yogurtaddons.macro.impl.potion.chestinput;

import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class InputChestManager {
    private List<BlockPos> chests = new ArrayList<>();

    public void addChest(BlockPos p) {
        chests.add(p);
    }

    public void remove(BlockPos p) {
        chests.remove(p);
    }

    public List<BlockPos> getAll() {
        return chests;
    }
}
