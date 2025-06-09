package net.ethann.yogurtaddons.failsafe;

import net.ethann.yogurtaddons.macro.MacroBase;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;

public class FailsafeManager {
    private final HashMap<Class<? extends FailsafeBase>, FailsafeBase> failsafes = new HashMap<>();
    public FailsafeManager() {
        Reflections reflections = new Reflections("net.ethann.yogurtaddons.failsafe.impl");
        Set<Class<? extends FailsafeBase>> classes = reflections.getSubTypesOf(FailsafeBase.class);

        for (Class<? extends FailsafeBase> clazz : classes) {
            try {
                FailsafeBase base = clazz.newInstance();
                failsafes.put(clazz, base);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("failed to initialize macro: " + clazz.getName());
            }
        }
    }

    public void enable() {

    }

    public void disable() {

    }

    public void triggerFailsafe(FailsafeBase failsafe) {

    }
}
