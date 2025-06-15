package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControlUtil {
    public static void rightClick() {
        Minecraft mc = Minecraft.getMinecraft();
        Method rightClickMethod;
        try {
            rightClickMethod = Minecraft.class.getDeclaredMethod("rightClickMouse");
        } catch (NoSuchMethodException e) {
            try {
                rightClickMethod = Minecraft.class.getDeclaredMethod("func_147121_ag");
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }

        rightClickMethod.setAccessible(true);
        try {
            rightClickMethod.invoke(mc);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void hold(Key key) {
        setKeyStatus(key.keyBinding, true);
    }

    public static void release(Key key) {
        setKeyStatus(key.keyBinding, false);
    }

    private static void setKeyStatus(KeyBinding key, boolean pressed) {
        if (!key.isKeyDown()) {

        }
        KeyBinding.setKeyBindState(key.getKeyCode(), pressed);
    }

    public enum Key {
        FORWARD(Minecraft.getMinecraft().gameSettings.keyBindForward),
        BACKWARD(Minecraft.getMinecraft().gameSettings.keyBindBack),
        SIDEWARD_LEFT(Minecraft.getMinecraft().gameSettings.keyBindLeft),
        SIDEWARD_RIGHT(Minecraft.getMinecraft().gameSettings.keyBindRight);

        private final KeyBinding keyBinding;

        Key(KeyBinding keyBinding) {
            this.keyBinding = keyBinding;
        }
    }



}
