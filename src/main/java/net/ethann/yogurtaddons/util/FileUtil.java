package net.ethann.yogurtaddons.util;

import net.ethann.yogurtaddons.YogurtAddons;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileUtil {
    public static File getConfigFileFromModPath(String name) {
        return new File(Loader.instance().getConfigDir(), YogurtAddons.MOD_ID + "/" + name);
    }

    public static File getConfigFromForgePath(String name) {
        return new File(Loader.instance().getConfigDir(),  name);
    }

    public static String readFile(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    }

    public static void writeFile(File file, String content) throws IOException {
        Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
    }
}
