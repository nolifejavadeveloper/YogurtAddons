package net.ethann.yogurtaddons.util;

import net.minecraft.client.Minecraft;

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


}
