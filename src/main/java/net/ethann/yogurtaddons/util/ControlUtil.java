package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControlUtil {
    private static boolean mouseUngrabbed = false;
    private static MouseHelper oldMouseHelper;

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
        KeyBinding.setKeyBindState(key.getKeyCode(), pressed);
    }

    public static void ungrabMouse() {
        Minecraft mc = Minecraft.getMinecraft();

        mc.gameSettings.pauseOnLostFocus = false;
        oldMouseHelper = mc.mouseHelper;
        oldMouseHelper.ungrabMouseCursor();
        mc.inGameHasFocus = true;

        mc.mouseHelper = new MouseHelper() {
            @Override
            public void mouseXYChange() {
            }

            @Override
            public void grabMouseCursor() {
            }

            @Override
            public void ungrabMouseCursor() {
            }
        };

        mouseUngrabbed = true;
    }

    public static void grabMouse() {
        if (!mouseUngrabbed) return;

        Minecraft mc = Minecraft.getMinecraft();

        if (oldMouseHelper != null) {
            mc.mouseHelper = oldMouseHelper;
        }

        mc.mouseHelper.grabMouseCursor();

        mouseUngrabbed = false;
    }


    public enum Key {
        FORWARD(Minecraft.getMinecraft().gameSettings.keyBindForward),
        BACKWARD(Minecraft.getMinecraft().gameSettings.keyBindBack),
        SIDEWARD_LEFT(Minecraft.getMinecraft().gameSettings.keyBindLeft),
        SIDEWARD_RIGHT(Minecraft.getMinecraft().gameSettings.keyBindRight),
        SHIFT(Minecraft.getMinecraft().gameSettings.keyBindSneak),
        OPEN_INVENTORY(Minecraft.getMinecraft().gameSettings.keyBindInventory),
        USE_ITEM(Minecraft.getMinecraft().gameSettings.keyBindUseItem),
        SLOT_1(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[0]),
        SLOT_2(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[1]),
        SLOT_3(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[2]),
        SLOT_4(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[3]),
        SLOT_5(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[4]),
        SLOT_6(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[5]),
        SLOT_7(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[6]),
        SLOT_8(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[7]),
        SLOT_9(Minecraft.getMinecraft().gameSettings.keyBindsHotbar[8]);

        private final KeyBinding keyBinding;

        Key(KeyBinding keyBinding) {
            this.keyBinding = keyBinding;
        }
    }



}
