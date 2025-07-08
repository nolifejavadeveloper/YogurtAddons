package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import java.util.ArrayList;
import java.util.Random;

public class CameraMover {
    private boolean shouldMoveYaw = false;
    private boolean shouldMovePitch = false;
    private float yawDelta = 0.0f;
    private float pitchDelta = 0.0f;
    private int index = 0;
    private ArrayList<Float> multipliers = new ArrayList<>();

    private EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

    public CameraMover(Float yaw, Float pitch) {
        if (yaw != null) {
            this.yawDelta = computeYawDelta(yaw);
            shouldMoveYaw = true;
        }

        if (pitch != null) {
            this.pitchDelta = computePitchDelta(pitch);
            shouldMovePitch = true;
        }

        ChatUtil.log("computed yaw delta: " + yawDelta);
        ChatUtil.log("computed pitch delta: " + pitchDelta);

        int totalTicks = (int) (Math.max(Math.abs(yawDelta), Math.abs(pitchDelta)) * 0.2 + Math.random() * 10);

        if (yaw == null && pitch == null) {
            ChatUtil.log("both yaw and pitch are null, not generating multipliers");
            return;
        }

        ArrayList<Double> unnormalizedMultipliers = new ArrayList<>();
        double sum = 0;
        for (double i = 0; i < 1; i += 1.0 / totalTicks) {
            double value = (float) cubic1DBezier(i, 0, 0.25, 0.75, 0);
            unnormalizedMultipliers.add(value);
            sum += value;
        }

        for (double value : unnormalizedMultipliers) {
            multipliers.add((float) (value / sum));
        }
    }

    private float computeYawDelta(float target) {
        float delta = target - player.rotationYaw;

        if (delta > 180.0f) {
            delta -= 360.0f;
        } else if (delta < -180.0f) {
            delta += 360.0f;
        }

        return delta;
    }

    private float computePitchDelta(float target) {
        return (target - player.rotationPitch) * 1;
    }

    public boolean tick() {
        if (index >= multipliers.size()) return false;
        if (shouldMoveYaw) player.rotationYaw += yawDelta * multipliers.get(index);
        if (shouldMovePitch) player.rotationPitch += pitchDelta * multipliers.get(index);

        ChatUtil.log("yaw change: " + yawDelta * multipliers.get(index) + " pitch change: " + pitchDelta * multipliers.get(index));

        index++;

        return true;
    }

    private static double cubic1DBezier(double t, double p0, double p1, double p2, double p3) {
        double u = 1 - t;
        return u*u*u * p0 +
                3 * u*u * t * p1 +
                3 * u * t*t * p2 +
                t*t*t * p3;
    }
}