package net.ethann.yogurtaddons.macro.impl.potion;

import net.ethann.yogurtaddons.util.ChatUtil;
import net.ethann.yogurtaddons.util.ControlUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ChestNavigator {
    private double target;
    private Direction dir;
    private ControlUtil.Key keyPressed;
    private boolean shouldBeOver = true;
    private final Runnable onFinish;

    public ChestNavigator(Runnable onFinish) {
        this.onFinish = onFinish;
    }

    public void enable() {
    }

    public void disable() {
    }

    public void next() {

    }

    public void go(Vec3 vec, Direction dir) {
        ChatUtil.log("enabling chestnavigator");
        EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;

        double target = 0.2 + Math.random() * 0.8;
        double curr = 0;
        switch (dir) {
            case X: {
                target += vec.xCoord;
                curr = p.posX;
                break;
            }
            case Z: {
                target += vec.zCoord;
                curr = p.posY;
                break;
            }
        }

        this.target = target;
        ChatUtil.log("target location computed: " + target);

        shouldBeOver = true;

        ControlUtil.Key key = dir.getKey(p.rotationYaw);
        if (curr > target) {
            // decrease value
            key = reverseKey(key);
            shouldBeOver = false;
        }

        ChatUtil.log("key computed, holding down " + key.name() + " key");

        ControlUtil.hold(key);
        keyPressed = key;
        this.dir = dir;


        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent e) {
        double curr = 0;
        switch (dir) {
            case X: {
                curr = Minecraft.getMinecraft().thePlayer.posX;
                break;
            }
            case Z: {
                curr = Minecraft.getMinecraft().thePlayer.posZ;
                break;
            }
        }

        if (curr > target) {
            if (shouldBeOver) {
                finish();
            }
        }else {
            if (!shouldBeOver) {
                finish();
            }
        }
    }

    private void finish() {
        ChatUtil.log("at target location, releasing key...");
        ControlUtil.release(keyPressed);
        ChatUtil.log("key " + keyPressed.name() + " released");

        ChatUtil.log("disabling chestnavigator");
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    private ControlUtil.Key reverseKey(ControlUtil.Key key) {
        switch (key) {
            case FORWARD: return ControlUtil.Key.BACKWARD;
            case BACKWARD: return ControlUtil.Key.FORWARD;
            case SIDEWARD_LEFT: return ControlUtil.Key.SIDEWARD_RIGHT;
            case SIDEWARD_RIGHT: return ControlUtil.Key.SIDEWARD_LEFT;
            default: return null;
        }
    }

    public enum Direction {
        X(ControlUtil.Key.SIDEWARD_LEFT, ControlUtil.Key.BACKWARD, ControlUtil.Key.SIDEWARD_RIGHT, ControlUtil.Key.FORWARD),
        Z(ControlUtil.Key.FORWARD, ControlUtil.Key.SIDEWARD_LEFT, ControlUtil.Key.BACKWARD, ControlUtil.Key.SIDEWARD_RIGHT);

        // keys that causes the value to increment based on yaw
        private final ControlUtil.Key yaw0;
        private final ControlUtil.Key yaw90;
        private final ControlUtil.Key yaw180;
        private final ControlUtil.Key yawN90;

        Direction(ControlUtil.Key yaw0, ControlUtil.Key yaw90, ControlUtil.Key yaw180, ControlUtil.Key yawN90) {
            this.yaw0 = yaw0;
            this.yaw90 = yaw90;
            this.yaw180 = yaw180;
            this.yawN90 = yawN90;
        }

        public ControlUtil.Key getKey(float yaw) {
            ControlUtil.Key key;
            switch ((int) yaw) {
                case 0: return yaw0;
                case 90: return yaw90;
                case 180:
                case -180:
                    return yaw180;
                case -90: return yawN90;
            }
            return null;
        }
    }

}
