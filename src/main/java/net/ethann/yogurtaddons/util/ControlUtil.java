package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControlUtil {
    private static final int REACH = 3;

    public static void rightClick() {
        Minecraft mc = Minecraft.getMinecraft();
        Method rightClickMethod;
        try {
            rightClickMethod = Minecraft.class.getDeclaredMethod("rightClickMouse");
            rightClickMethod.setAccessible(true);
            rightClickMethod.invoke(mc);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


}
