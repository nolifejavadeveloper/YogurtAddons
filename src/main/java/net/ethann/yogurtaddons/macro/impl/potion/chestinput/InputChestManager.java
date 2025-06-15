package net.ethann.yogurtaddons.macro.impl.potion.chestinput;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.ethann.yogurtaddons.util.FileUtil;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InputChestManager {
    private static final String FILE_NAME = "chestInputs.json";
    private List<BlockPos> chests = new ArrayList<>();

    public InputChestManager() {
        load();
    }

    private void addChest(BlockPos p) {
        chests.add(p);
    }

    private void removeChest(BlockPos p) {
        chests.remove(p);
    }

    public void toggleChest(BlockPos p) {
        if (chests.contains(p)) {
            removeChest(p);
        }else {
            addChest(p);
        }
    }

    public List<BlockPos> getAll() {
        return chests;
    }

    private void load() {
        File file = FileUtil.getConfigFileFromModPath(FILE_NAME);
        String content;
        try {
            content = FileUtil.readFile(file);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read " + FILE_NAME + ": " + e.getMessage());
        }

        Type type = new TypeToken<List<BlockPos>>() {}.getType();
        Gson json = new Gson();

        chests = json.fromJson(content, type);
    }

    private void save() {
        File file = FileUtil.getConfigFileFromModPath(FILE_NAME);
        Gson json = new GsonBuilder().setPrettyPrinting().create();

        String content = json.toJson(chests);
        try {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            FileUtil.writeFile(file, content);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write " + FILE_NAME + ": " + e.getMessage());
        }
    }
}
