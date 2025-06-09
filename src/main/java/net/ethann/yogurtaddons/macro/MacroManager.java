package net.ethann.yogurtaddons.macro;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;

public class MacroManager {
    private final HashMap<Class<? extends MacroBase>, MacroBase> macros = new HashMap<>();

    public MacroManager() {
        Reflections reflections = new Reflections("net.ethann.yogurtaddons.macro.impl");
        Set<Class<? extends MacroBase>> classes = reflections.getSubTypesOf(MacroBase.class);

        for (Class<? extends MacroBase> clazz : classes) {
            try {
                MacroBase base = clazz.newInstance();
                macros.put(clazz, base);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("failed to initialize macro: " + clazz.getName());
            }
        }
    }

    public MacroBase getMacro(Class<? extends MacroBase> clazz) {
        return macros.get(clazz);
    }

    public void startMacro(Class<? extends MacroBase> clazz) {
        //TODO: start failsafe
        macros.get(clazz).enable();
    }

    public void stopMacro(Class<? extends MacroBase> clazz) {
        //TODO: stop failsafe
        macros.get(clazz).disable();
    }
}
