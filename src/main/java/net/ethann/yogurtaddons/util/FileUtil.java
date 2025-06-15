package net.ethann.yogurtaddons.util;

import net.ethann.yogurtaddons.YogurtAddons;
import net.minecraftforge.fml.common.Loader;

import java.io.File;

public class FileUtil {
    public static File getConfigFileFromModPath(String name) {
        return new File(Loader.instance().getConfigDir(), YogurtAddons.MOD_ID + "/" + name);
    }

    public static File getConfigFromForgePath(String name) {
        return new File(Loader.instance().getConfigDir(),  name);
    }
}
