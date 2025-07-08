package net.ethann.yogurtaddons.feature.impl.potionrefiller;

import net.ethann.yogurtaddons.feature.impl.chestseller.AutoChestSellerCommand;
import net.ethann.yogurtaddons.feature.impl.potionrefiller.chestinput.ChestInput;
import net.ethann.yogurtaddons.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.function.Consumer;

public class ChestNavigator {
//    private double target;
//    private Direction dir;
//    private ChestInput lastPos;
//    private ChestInput nextPos;
//    private ControlUtil.Key keyPressed;
//    private boolean shouldBeOver = true;
//    private final Consumer<Boolean> onFinish;
//
//    private final Scheduler scheduler;
//
//    private MovementStage stage;
//
//    private boolean pendingSneak = false;
//    private boolean pendingUnsneak = false;
//
//    private CameraMover cm;
//
//    public ChestNavigator(Consumer<Boolean> onFinish) {
//        this.onFinish = onFinish;
//        this.scheduler = new Scheduler();
//
//
//    }
//
//    protected void onEnable() {
//    }
//
//    protected void onDisable() {
//        ChatUtil.log("disabling chestnavigator");
//        MinecraftForge.EVENT_BUS.unregister(this);
//    }
//
//    private void navigateDirection(ChestInput pos, Direction dir)  {
//        ChatUtil.log("enabling chestnavigator");
//        pendingUnsneak = false;
//        pendingSneak = false;
//        EntityPlayerSP p = Minecraft.getMinecraft().thePlayer;
//
//        ChatUtil.log("going to " + pos.pos + " towards " + dir.name());
//
//        double target = 0.3 + Math.random() * 0.5;
//        double curr = 0;
//        switch (dir) {
//            case X: {
//                target += pos.pos.getX();
//                curr = p.posX;
//                break;
//            }
//            case Z: {
//                target += pos.pos.getZ();
//                curr = p.posZ;
//                break;
//            }
//        }
//
//        this.target = target;
//        ChatUtil.log("target location computed: " + target);
//
//        shouldBeOver = true;
//
//        ControlUtil.Key key = dir.getKey(p.rotationYaw % 360);
//        if (key == null) {
//            ChatUtil.error("unable to find key by direction. wrong rotation?");
//            d();
//            onFinish.accept(false);
//            return;
//        }
//        if (curr > target) {
//            // decrease value
//            key = reverseKey(key);
//            if (key == null) {
//                ChatUtil.error("unable to compute reverse key. key is null?");
//                disable();
//                onFinish.accept(false);
//                return;
//            }
//            shouldBeOver = false;
//        }
//
//        ChatUtil.log("key computed, holding down " + key.name() + " key");
//
//        ControlUtil.hold(key);
//
//        scheduler.schedule(DelayUtil.getDelay(0, 70));
//        pendingSneak = true;
//        ChatUtil.log("sneak queued");
//
//        keyPressed = key;
//        this.dir = dir;
//    }
//
//    private void move() {
//        if (lastPos == null) {
////            if (matchDirection(player.posX, pos.getX())) {
////                dir = Direction.X;
////            }else if (matchDirection(player.posZ, pos.getZ())) {
////                dir = Direction.Z;
////            }else {
////                onFinish.accept(false);
////                ChatUtil.error("unable to find path");
////                return;
////            }
//            dir = Direction.X;
//        }else {
//            if (matchDirection(lastPos.pos.getX(), nextPos.pos.getX())) {
//                dir = Direction.Z;
//            }else if (matchDirection(lastPos.pos.getZ(), nextPos.pos.getZ())) {
//                dir = Direction.X;
//            }else {
//                onFinish.accept(false);
//                ChatUtil.error("unable to find path");
//                return;
//            }
//        }
//
//        navigateDirection(nextPos, dir);
//    }
//
//    public void go(ChestInput pos) {
//        nextPos = pos;
//        ChatUtil.log("chestPos detected: " + pos.orientation);
//        if (lastPos == null || nextPos.orientation != lastPos.orientation) {
//            ChatUtil.log("angle adjustment required");
//            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
//            stage = MovementStage.ROTATE_ANGLE;
//            if ((pos.orientation.getYaw() == null || pos.orientation.getYaw() == (int) player.rotationYaw) && (pos.orientation.getPitch() == null || pos.orientation.getPitch() == (int) player.rotationPitch)) {
//                ChatUtil.log("angle requirement check passed, skipping rotation...");
//                cm = new CameraMover(null, null);
//            }else {
//                cm = new CameraMover(pos.orientation.getYaw() == null ? null : pos.orientation.getYaw().floatValue(), pos.orientation.getPitch() == null ? null : pos.orientation.getPitch().floatValue());
//            }
//        }else {
//            stage = MovementStage.MOVE_PLAYER;
//            move();
//        }
//        MinecraftForge.EVENT_BUS.register(this);
//    }
//
//    private boolean matchDirection(double a, int b) {
//        return Math.floor(a) == b;
//    }
//
//    @SubscribeEvent
//    public void onTick(TickEvent.ClientTickEvent e) {
//        switch (stage) {
//            case MOVE_PLAYER: {
//                if (pendingSneak && scheduler.isOver()) {
//                    ControlUtil.hold(ControlUtil.Key.SHIFT);
//                    ChatUtil.log("started sneaking");
//                    pendingSneak = false;
//                }
//
//                if (pendingUnsneak && scheduler.isOver()) {
//                    ControlUtil.release(ControlUtil.Key.SHIFT);
//                    ChatUtil.log("stopped sneaking");
//                    stage = MovementStage.FINISH;
//                    pendingUnsneak = false;
//                    finish();
//                }
//
//                double curr = 0;
//                switch (dir) {
//                    case X: {
//                        curr = Minecraft.getMinecraft().thePlayer.posX;
//                        break;
//                    }
//                    case Z: {
//                        curr = Minecraft.getMinecraft().thePlayer.posZ;
//                        break;
//                    }
//                }
//
//                if (curr > target) {
//                    if (shouldBeOver) {
//                        finish();
//                    }
//                }else {
//                    if (!shouldBeOver) {
//                        finish();
//                    }
//                }
//                break;
//            }
//
//            case ROTATE_ANGLE: {
//                if (!cm.tick()) {
//                    finish();
//                }
//            }
//        }
//    }
//
//    private void finish() {
//        if (!pendingUnsneak) {
//            if (stage == MovementStage.MOVE_PLAYER) {
//                ChatUtil.log("at target location, releasing key...");
//                if (keyPressed == null) return;
//                ControlUtil.release(keyPressed);
//                ChatUtil.log("key " + keyPressed.name() + " released");
//
//                pendingUnsneak = true;
//                scheduler.schedule(DelayUtil.getDelay(0, 70));
//                ChatUtil.log("unsneak queued");
//                return;
//            }
//        }
//
//        lastPos = nextPos;
//
//        disable();
//
//        ChatUtil.log("navigation completed");
//
//        onFinish.accept(true);
//    }
//
//    private ControlUtil.Key reverseKey(ControlUtil.Key key) {
//        switch (key) {
//            case FORWARD: return ControlUtil.Key.BACKWARD;
//            case BACKWARD: return ControlUtil.Key.FORWARD;
//            case SIDEWARD_LEFT: return ControlUtil.Key.SIDEWARD_RIGHT;
//            case SIDEWARD_RIGHT: return ControlUtil.Key.SIDEWARD_LEFT;
//            default: return null;
//        }
//    }
//
//    private enum MovementStage {
//        ROTATE_ANGLE,
//        MOVE_PLAYER,
//        FINISH;
//    }
//
//    public enum Direction {
//        X(ControlUtil.Key.SIDEWARD_LEFT, ControlUtil.Key.BACKWARD, ControlUtil.Key.SIDEWARD_RIGHT, ControlUtil.Key.FORWARD),
//        Z(ControlUtil.Key.FORWARD, ControlUtil.Key.SIDEWARD_LEFT, ControlUtil.Key.BACKWARD, ControlUtil.Key.SIDEWARD_RIGHT);
//
//        // keys that causes the value to increment based on yaw
//        private final ControlUtil.Key yaw0;
//        private final ControlUtil.Key yaw90;
//        private final ControlUtil.Key yaw180;
//        private final ControlUtil.Key yawN90;
//
//        Direction(ControlUtil.Key yaw0, ControlUtil.Key yaw90, ControlUtil.Key yaw180, ControlUtil.Key yawN90) {
//            this.yaw0 = yaw0;
//            this.yaw90 = yaw90;
//            this.yaw180 = yaw180;
//            this.yawN90 = yawN90;
//        }
//
//        public ControlUtil.Key getKey(float yaw) {
//            int roundedYaw = Math.round(yaw);
//            switch (roundedYaw) {
//                case 0: return yaw0;
//                case 90: return yaw90;
//                case 180:
//                case -180:
//                    return yaw180;
//                case -90:
//                case 270:
//                    return yawN90;
//                default: {
//                    ChatUtil.error("unable to find key for yaw: " + yaw + " after round: " + roundedYaw);
//                }
//            }
//            return null;
//        }
//    }
}
