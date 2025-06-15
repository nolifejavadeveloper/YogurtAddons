package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

public class PlayerUtil {
    private static final int REACH = 10;
    public static BlockPos getBlockLookingAt() {
        EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
        net.minecraft.util.Vec3 eyePos = p.getPositionEyes(1.0F);
        net.minecraft.util.Vec3 lookVec = p.getLookVec();
        net.minecraft.util.Vec3 reachVec = eyePos.addVector(lookVec.xCoord * REACH, lookVec.yCoord * REACH, lookVec.zCoord * REACH);
        MovingObjectPosition mop =  p.worldObj.rayTraceBlocks(eyePos, reachVec, false, false, false);
        if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            return mop.getBlockPos();
        }

        return null;
    }
}
